//
//  RecordingViewController.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import UIKit
import SceneKit
import AVFoundation

class RecordingViewController: UIViewController, UITextFieldDelegate{

    @IBOutlet weak var dateTextField: UITextField!
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var titleTextField: UITextField!
    
    @IBOutlet weak var recordButton: UIButton!
    
    var count = 0
    
    var recordingModelRef : RecordingModel?
    var settingsModelRef : SettingsModel?
    var theAppDelegate : AppDelegate?
    
    let audioFilename = "recording\(Date().timeIntervalSince1970).wav"
    
    var audioRecorder: AVAudioRecorder? = nil
    
    var audioFileURL: URL?
    var audioPlayer: AVAudioPlayer? = nil
    var audioEngine = AVAudioEngine()
    var audioPlayerNode = AVAudioPlayerNode()
    var audioUnitTimePitch = AVAudioUnitTimePitch()
    
    @IBAction func recordPressed(_ sender: Any) {
        if audioRecorder != nil{
        
            let uTitle = titleTextField.text ?? "default"
            let uDate = dateTextField.text ?? "default"
            let uName = nameTextField.text ?? "default"
            recordingModelRef?.addRecording(title: uTitle, date: uDate, name: uName, fileURL: audioFileURL, fileName: audioFilename)
            recordingModelRef?.save()
            // Stop recording
            audioRecorder!.stop()
            audioRecorder = nil
            
            titleTextField.text = nil
            dateTextField.text = nil
            nameTextField.text = nil
            
            recordButton.setTitle("Record", for: .normal)
                        
        } else {
            // Start recording
            audioFileURL = getAudioFileUrl()
            let settings = [
                AVFormatIDKey: Int(kAudioFormatLinearPCM),
                AVSampleRateKey: 44100,
                AVNumberOfChannelsKey: 1,
                AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
            ]
            audioRecorder = try? AVAudioRecorder(url: audioFileURL!, settings: settings)
            audioRecorder?.record()
            recordButton.setTitle("Stop", for: .normal)
        }
    }
    
    func getAudioFileUrl() -> URL? {
        let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        let audioFilePath = documentsPath.appending("/\(audioFilename)")
        return URL(fileURLWithPath: audioFilePath)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder();
            return true;
        }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // Reload the settings model
        self.settingsModelRef = self.theAppDelegate?.settingsData
        self.viewDidLoad()
    }
    
    
    override func viewDidLoad() {
        
        dateTextField.delegate = self
        nameTextField.delegate = self
        titleTextField.delegate = self
        
        do {
            let session = AVAudioSession.sharedInstance()
            try session.setCategory(.playAndRecord, mode: .default, options: [])
            try session.setActive(true)
        } catch let error {
            print("Failed to configure AVAudioSession: \(error.localizedDescription)")
        }
        
                self.theAppDelegate = UIApplication.shared.delegate as? AppDelegate
                self.recordingModelRef = self.theAppDelegate?.recordingData
                self.settingsModelRef = self.theAppDelegate?.settingsData
                super.viewDidLoad()
        
        
        // Request microphone permission
            AVAudioSession.sharedInstance().requestRecordPermission { (granted) in
                if !granted {
                }
            }
        // Set up SceneKit view
            let sceneView = SCNView(frame: CGRect(x: 160, y: 160, width: 100, height: 100))
            view.addSubview(sceneView)
            
        
            let scene = SCNScene()
            
            let sphereGeometry = SCNSphere(radius: 1)
            
            let sphereMaterial = SCNMaterial()
            sphereMaterial.diffuse.contents = UIColor.red
            
            sphereGeometry.materials = [sphereMaterial]
            
            let sphereNode = SCNNode(geometry: sphereGeometry)
            sphereNode.position = SCNVector3(x: 0, y: 0, z: -10)
            
            // Add the sphere node to the scene
            scene.rootNode.addChildNode(sphereNode)
        
            if(settingsModelRef?.animation != "n"){
                
                let duration = 0.05
                let amplitude = 0.5
                
                let left = SCNAction.moveBy(x: -amplitude, y: 0, z: 0, duration: duration / 0.5)
                let right = SCNAction.moveBy(x: amplitude, y: 0, z: 0, duration: duration)
                let shakeAction = SCNAction.repeatForever(SCNAction.sequence([left, right]))
                sphereNode.runAction(shakeAction)
            }
            // Set the scene to the view
            sceneView.scene = scene
        
    }

}
