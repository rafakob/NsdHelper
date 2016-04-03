[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-NsdHelper-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3167)
[![](https://jitpack.io/v/rafakob/NsdHelper.svg)](https://jitpack.io/#rafakob/NsdHelper)


# NsdHelper #
**NsdHelper** is a wrapper/helper library for [Android Network Service Discovery](http://developer.android.com/training/connect-devices-wirelessly/nsd.html). I've created it for my own purpose (mostly for discovering HTTP services in LAN) to simplify the whole NSD process. I also added couple of nice features (timeout, detailed log) and fixed a [#71367](https://code.google.com/p/android/issues/detail?id=71367) using simple FIFO queue. All public methods are well documented so it should be pretty straightforward to use.


> **Important!**

> Please note that it still under development and needs couple of tests. If you find any bugs please post an issue or create a pull request.

## Install ##
Add it in your root build.gradle at the end of repositories:
```java
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Add the dependency:

[![](https://jitpack.io/v/rafakob/NsdHelper.svg)](https://jitpack.io/#rafakob/NsdHelper)


```java
dependencies {
        compile 'com.github.rafakob:NsdHelper:VERSION'
}
```
## How to use it ##
### Setup  and config ###
Class ```NsdHelper``` requires activity context in order to create ```NsdManager``` instance. There is only one listener (```NsdListener```) for all NSD actions (registration, discovery, resolve):

```java
public class MainActivity extends AppCompatActivity implements NsdListener {
    NsdHelper nsdHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nsdHelper = new NsdHelper(this, this);
        nsdHelper.setLogEnabled(true);
        nsdHelper.setAutoResolveEnabled(true);
        nsdHelper.setDiscoveryTimeout(30);
        ...
    }
    ...
}
```
Configuration methods:
- ```setLogEnabled(boolean enabled)``` - enable logcat messages (default: false)
- ```setAutoResolveEnabled(boolean enabled)``` - resolve service immediately after it was discovered (default: true)
- ```setDiscoveryTimeout(int seconds)``` - if no new service has been discovered for a timeout interval, discovering will be stopped


Dont forget about internet permissions:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

### Registration ###
```java
nsdHelper.registerService("Chat", NsdType.HTTP);
...
nsdHelper.unregisterService();
```
### Discovery ###
```java
nsdHelper.startDiscovery(NsdType.HTTP);
...
nsdHelper.stopDiscovery();
```
### Resolve ###
```java
nsdHelper.resolveService(nsdService);
```
### Listeners ###
There is only one listener common for all actions. Implement ```NsdListener``` interface:
```java
@Override
public void onNsdRegistered(NsdService registeredService) {
}

@Override
public void onNsdDiscoveryFinished() {
}

@Override
public void onNsdServiceFound(NsdService foundService) {
}

@Override
public void onNsdServiceResolved(NsdService resolvedService) {
}

@Override
public void onNsdServiceLost(NsdService lostService) {
}

@Override
public void onNsdError(String errorMessage, int errorCode, String errorSource) {
}
```

### Example logcat output ###

```
D/NsdHelper: Service discovery started.
D/NsdHelper: Service found -> Kodi (OpenELEC)
D/NsdHelper: Service found -> ds115
D/NsdHelper: Service resolved -> Kodi (OpenELEC), OpenElec.lan/192.168.1.110, port 80, ._http._tcp
D/NsdHelper: Service resolved -> ds115, DS115.lan/192.168.1.102, port 5000, ._http._tcp
D/NsdHelper: Registered -> Chat
D/NsdHelper: Service discovery stopped.
D/NsdHelper: Unregistered -> Chat
```


### License ###
```
Copyright 2016 Rafał Kobyłko

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
