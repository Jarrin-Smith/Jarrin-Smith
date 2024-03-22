//
//  RecordingModel.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import Foundation
import AVFoundation

class RecordingModel: Codable {
    
    var title: String
    var date: String
    var name: String
    var fileURL: URL?
    var fileName: String
    var recordings = [RecordingModel]()
    
    init(title: String, date: String, name: String, fileURL: URL?, fileName: String) {
        self.title = title
        self.date = date
        self.name = name
        self.fileURL = fileURL
        self.fileName = fileName
    }
    
    func addRecording(title: String, date: String, name: String, fileURL: URL?, fileName: String) {
        let recording = RecordingModel(title: title, date: date, name: name, fileURL: fileURL, fileName: fileName)
        recordings.append(recording)
        recordings = recordings.sorted(by: { (recording1, recording2) -> Bool in
            save()
            return recording1.title < recording2.title
        })
    }
    
    func save() {
        let fileURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("recordings.plist")
        
        do {
            let encoder = PropertyListEncoder()
            let data = try encoder.encode(self)
            try data.write(to: fileURL, options: .atomic)
            print("Saved data to \(fileURL.path)")
        } catch {
            print("Error saving recordings data: \(error.localizedDescription)")
        }
    }
    
    static func load() -> RecordingModel? {
        let fileURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("recordings.plist")
        
        
        if let data = try? Data(contentsOf: fileURL) {
            let decoder = PropertyListDecoder()
            if let model = try? decoder.decode(RecordingModel.self, from: data) {
                return model
            }
        }
        
        return nil
    }
    func removeRecording(_ recording: RecordingModel) {
        if let index = recordings.firstIndex(where: { $0 === recording }) {
            recordings.remove(at: index)
            save()
        }
    }
}

