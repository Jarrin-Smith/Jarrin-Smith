//
//  AppDelegate.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    let recordingData : RecordingModel = RecordingModel(title: "", date: "", name: "", fileURL: URL(string: ""), fileName: "")
    
    let settingsData : SettingsModel = SettingsModel(animation: "y", speed: 1)
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        
        if let savedData = RecordingModel.load() {
            recordingData.name = savedData.name
            recordingData.title = savedData.title
            recordingData.date = savedData.date
            recordingData.fileURL = savedData.fileURL
            recordingData.recordings = savedData.recordings
            }
         
            return true
        }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

