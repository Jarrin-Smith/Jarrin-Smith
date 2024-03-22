//
//  TableViewController.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import UIKit
import AVFoundation
import ARKit

class TableViewController: UITableViewController {
    
    let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    
    var recordingModelRef : RecordingModel?
    var theAppDelegate : AppDelegate?
    var settingsModelRef : SettingsModel?
    
    var audioPlayer: AVAudioPlayer? = nil
    
    override func viewDidLoad() {
        
        self.theAppDelegate = UIApplication.shared.delegate as? AppDelegate
        self.recordingModelRef = self.theAppDelegate?.recordingData
        self.settingsModelRef = self.theAppDelegate?.settingsData
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        guard let recordingModelRef = recordingModelRef else {
            return
        }
        
        tableView.rowHeight = 120
        tableView.reloadData()
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let firstLetter = recordingModelRef?.recordings[section].title.prefix(1).uppercased()
        let recordingsInSection = recordingModelRef?.recordings.filter { String($0.title.prefix(1)).uppercased() == firstLetter }
        return recordingsInSection?.count ?? 0
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return recordingModelRef?.recordings.count ?? 0
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        let firstLetters = Array(Set(recordingModelRef!.recordings.map { String($0.title.prefix(1)).uppercased() })).sorted()
        
        guard section < firstLetters.count else {
            return nil
        }
        
        return firstLetters[section]
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = indexPath.section
        let row = indexPath.row
        let firstLetter = tableView.dataSource?.tableView!(tableView, titleForHeaderInSection: section)
        let recordingsInSection = recordingModelRef!.recordings.filter { String($0.title.prefix(1)).uppercased() == firstLetter }
        let recording = recordingsInSection[row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! TableViewCell
        
        cell.titleLabel.text! = recording.title
        cell.dateLabel.text! = recording.date
        cell.nameLabel.text! = recording.name
        
        cell.playButton.tag = indexPath.row
        cell.playButton.addTarget(self, action: #selector(playButtonTapped(_:)), for: .touchUpInside)
        return cell
    }
    
    @IBAction func playButtonTapped(_ sender: UIButton) {
        guard let cell = sender.superview?.superview as? TableViewCell else {
            return
        }
        
        guard let indexPath = tableView.indexPath(for: cell) else {
            return
        }
        
        let section = indexPath.section
        let row = indexPath.row
        let firstLetter = tableView.dataSource?.tableView!(tableView, titleForHeaderInSection: section)
        let recordingsInSection = recordingModelRef!.recordings.filter { String($0.title.prefix(1)).uppercased() == firstLetter }
        let recording = recordingsInSection[row]
        guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return
        }
        let audioFilePath = documentsDirectory.appendingPathComponent(recording.fileName)
        
        if let audioFileURL = recording.fileURL {
            do {
                audioPlayer = try AVAudioPlayer(contentsOf: audioFilePath)
                audioPlayer?.enableRate = true
                // 0-2+ for rate changes
                if(settingsModelRef?.speed != nil){
                    audioPlayer?.rate = settingsModelRef!.speed
                    audioPlayer?.play()
                }
                else{
                    audioPlayer?.play()
                }
            } catch let error as NSError {
                print("Error playing audio: \(error.localizedDescription)")
            }
        } else {
            print("No audio file to play")
        }
    }
    
    @IBAction func deleteButtonTapped(_ sender: UIButton) {
        guard let indexPath = tableView.indexPathForRow(at: sender.convert(CGPoint.zero, to: tableView)) else {
            return
        }
        recordingModelRef?.recordings.remove(at: indexPath.row)
        recordingModelRef?.save()
        tableView.reloadData()
    }
}
