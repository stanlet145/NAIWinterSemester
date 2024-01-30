# LAB 7
 1) Using conda to setup Gymnasium with Python 3.11 environment for Windows 11 <img width="778" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/fbc50ebf-7e61-43da-8d5c-a8ab444019af">
 2) Coding and testing solution - FrozenLake-v1

    Usage of Reinforcement Learning with Gymnasium: FrozenLake-v1
    Robot makes action (0-3) and receives reward for successful state

    Code implements epsilon greedy algorithm -  common exploration strategy used in reinforcement learning.
    It balances exploration and exploitation to help agents learn more effectively
    it helps to discover rewards he otherwise would not
    
 4) https://gifyu.com/image/SCKvR
Solution presentation shows training for FrozenLake is_slippery parameter - false and true, which indicates if robot will randomly slip and change position into random direction.
It reinforces it to reevaluate which direction to take to get reward.
algorithm uses QTable as table of values that represent if position/action on table was rewarded or not. The bigger value in table the better.
After saving training results presentation shows how despite is_slippery parameter being True, robot successfuly makes it into reward into single try

# LAB 6
  this application will output ALL frames containing
  POLISH , RUSSIAN, USA Flags hidden in .mp4 file video
  using color analysis in Java with OpenCV
  <p>
  Using OpenCV Java library. Program will create color masks and
  will try to see if certain frame of video contains proper or higher than threshold
  amount of pixels matching mask colors

  if it does it will output this frames to /data directory

 * * @author s12901 Stanisław Kibort for NAI winter semester 2023/2024
<img width="1481" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/1aa73acd-cdeb-406c-a290-46fb29b52830">


# LAB 5 
Classify data from csv, train and test neural network using Deeplearing4j java library<br />
author: Stanisław Kibort **s12901**<br />
Evaluation for model based on banknote authentication dataset<br /> using **Deeplearing4j** library<br />
<br />
**confusion and evaluation matrix for data pool of 50 :**
<img width="1211" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/8ac7991f-2433-412c-b955-31c041817cea">
**confusion and evaluation matrix for data pool of 1300 :**
<img width="1224" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/82321a8e-5df4-4eb8-8a23-ec9372e7b548">

**MNIST - Train Neural Network to distinguish animals video example:**
https://youtu.be/0Uv8fVGNeXw

**MNIST - Gender Classification training: **
<img width="1453" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/33e75b99-114d-40d7-9dbd-5dfea3f73456">

Running NeuralNetwork module - 
Install Java on your computer (JDK 1.8)
download and build project using Maven.
After installing required dependencies run it from IDE:
<br />
<img width="555" alt="image" src="https://github.com/stanlet145/NAIWinterSemester/assets/57921350/d570ebd0-0044-489b-9c09-6d55969321a2">
<br />
or package it and run using java -jar (jar_name) to train neural networks
