C323/Spring 2023 Final Project
Ethan Stone (ethstone@iu.edu) and Jarrin Smith (jks7@iu.edu)

Working our app:
Our app will record audio clips and save them to a table view. You can also change a few settings of the clips. You start by inputting the title of the song, your name,
and the date, then hit record. You will then speak into the phone and stop the recording whenever you are done. It will then save the table view where you can play
the clip or delete it. You can also use the settings view to start/stop the animation on the first page and change the playback speed of the audio.

XCode environment:
We used XCode 14.2. The app works best with an actual ios device (ideally iPhone XR or higher)

Required Features and Functionalities:
Our app follows the Model-View-Controller architectural pattern by using swift classes that are either a Model, a View, or a Controller.
Our User Interface has three views, that you can switch between using a TabBarController. The first view has a label indicating it is the recording view, three buttons,
and three text fields for inputting the title, name and date. The second view is a TableView that uses a custom cell to show all of the audio clips we have recorded,
and their title, name and date. In this view you can play or delete any of the audio clips. The final view is the settings view, which has a label indicating it is the 
settings, and two buttons+two text fields where you can input data to edit the audio clip.
We used persistant storage to save all of the audio clips in the table view when the app closes and re opens. This appears in the RecordingModel and the AppDelegate.
We used AVFoundation to record the audio clips. This appears in the RecordingModel and the RecordingViewController.
We used AVPlayer to play back the audio clip. This appears in the TableViewController. We decided to use this after assignment 4 instead of User Notifications because we 
felt it was more applicable than notifications in our app.
We used SceneKit to have an animation on the recording view. This appears in the RecordingViewController. We decided to use this instead of ARKit because we were having
difficulties implementing ARKit. We decided to do this after assignment 4.

Responsibilities:
Jarrin - Recording audio, TableViewController, and SceneKit animation.
Ethan - Persistant Storage, Settings model/controller, and helped with Recording.
