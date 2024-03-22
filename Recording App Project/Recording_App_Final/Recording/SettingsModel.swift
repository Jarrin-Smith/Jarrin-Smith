//
//  SettingsModel.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import Foundation

class SettingsModel{
    
    var animation: String
    var speed: Float
    
    init(animation: String, speed: Float) {
        self.animation = animation
        self.speed = speed
    }
}
