# NourseCompose
Simple project to save data on temperature and blood oxygenation, and create alarms so you don't forget to take your medicine, using mvvm, jetpack compose and room.


### Description

<p>
  A simple app to be able to save data such as blood oxygen and temperature and to be able to observe this data in a graph, it is saved in the device's memory using room, in addition to being able to add alarms or reminders to take medications, alarms they can be just notifications or full alarms.
</p>

<p>
  Image compression (to save a compressed copy of the alarm image) is also used, as well as the AlarmManager to log the alarms and the Broadcast receiver to get the alarm signal.
</p>

<p>
  Hilt is very useful to be able to inject the dependencies where it is needed, in addition to using a data store to be able to save user configurations and take advantage of all the power of flows.
</p>

## Note

For correct operation it is necessary to **disable battery optimization**, for some reason the BroadcastReceiver does not receive the alarm signal if optimization is not disabled

<p>
  <img src="https://i.imgur.com/Jtl9Elz.png" alt="auth" width="200"/>
</p>

<br>

### Main screen (empty)
<p>
  <img src="https://i.imgur.com/lMiUkri.png" alt="empty alarm" width="200"/>
  <img src="https://i.imgur.com/yIRNZsS.png" alt="empty oxygen" width="200"/>
  <img src="https://i.imgur.com/CuzTM2i.png" alt="empty temperature" width="200"/>
 </p>
 
 ### Add new alarm
 
 <p>
  <img src="https://i.imgur.com/PpcTgHO.png" alt="name alarm" width="200"/>
  <img src="https://i.imgur.com/uIsWU8v.png" alt="description oxygen" width="200"/>
  <img src="https://i.imgur.com/kRfgjdn.png" alt="type repeat" width="200"/>
   <img src="https://i.imgur.com/kRfgjdn.png" alt="type repeat" width="200"/>
   <img src="https://i.imgur.com/IwiFD81.png" alt="screen time" width="200"/>
 </p>
 
 #### Pickers
 
 <p>
  <img src="https://i.imgur.com/CYUEV9X.png" alt="select hour" width="200"/>
  <img src="https://i.imgur.com/zea0cVR.png" alt="select time" width="200"/>
  <img src="https://i.imgur.com/WId6c1L.png" alt="select range" width="200"/>
 </p>
 
 ### Main screen (no empty)
<p>
  <img src="https://i.imgur.com/Yd6CAgk.png" alt="screen alarm" width="200"/>
  <img src="https://i.imgur.com/h0XlUR7.png" alt="screen oxygen" width="200"/>
  <img src="https://i.imgur.com/1zjLC4X.png" alt="screen temperature" width="200"/>
 </p>
 
 ### Others screen
<p>
  <img src="https://i.imgur.com/Rw0JNAb.png" alt="click alarm notify screen" width="200"/>
  <img src="https://i.imgur.com/HJTo9HD.png" alt="logs screen" width="200"/>
  <img src="https://i.imgur.com/PNYF2Gx.png" alt="config screen" width="200"/>
  <img src="https://i.imgur.com/DtRsAXN.png" alt="notify" width="200"/>
  <img src="https://i.imgur.com/bMSql5a.png" alt="alarm screen" width="200"/>
 </p>
 
