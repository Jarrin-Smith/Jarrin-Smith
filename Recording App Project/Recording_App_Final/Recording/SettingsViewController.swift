//
//  SettingsViewController.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import UIKit

class SettingsViewController: UIViewController, UITextFieldDelegate{
    
    @IBOutlet weak var speedTextField: UITextField!
    @IBOutlet weak var speedButton: UIButton!
    @IBOutlet weak var animationTextField: UITextField!
    @IBOutlet weak var animationButton: UIButton!
    
    var settingsModelRef : SettingsModel?
    var recordingModelRef : RecordingModel?
    var theAppDelegate : AppDelegate?
    
    
    @IBAction func pitchButton(_ sender: Any) {
        if (animationTextField.text?.lowercased() == "y" || animationTextField.text?.lowercased() == "n"){
            settingsModelRef?.animation = animationTextField.text ?? "n"
            animationTextField.text = nil
        }
    }
    @IBAction func speedButton(_ sender: Any) {
        if let speedString = speedTextField.text, let speedValue = Float(speedString) {
            settingsModelRef?.speed = speedValue
            speedTextField.text = nil
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder();
            return true;
        }
    
    override func viewDidLoad() {
        self.theAppDelegate = UIApplication.shared.delegate as? AppDelegate
        self.recordingModelRef = self.theAppDelegate?.recordingData
        self.settingsModelRef = self.theAppDelegate?.settingsData
    
        // Show the notification
        let alert = UIAlertController(title: "Settings Instructions", message: "Enter values in the text fields and tap the corresponding button to save them.", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        present(alert, animated: true, completion: nil)
        
        // Mark the notification as shown
        UserDefaults.standard.set(true, forKey: "SettingsNotificationShown")
        
        speedTextField.delegate = self
        animationTextField.delegate = self
        
        animationTextField.text = "Y or N"
        super.viewDidLoad()
    }
}
