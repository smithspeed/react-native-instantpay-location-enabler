# react-native-instantpay-location-enabler

React Native module for checking Device Location is Enable or Disable. Support only for Android.

<img src="dialogPermission.png" width="100%" />


## TOC

- [Installation](#installation)
- [Manual Installation](#manual-installation)
- [Usage](#usage)

## Installation

```sh
npm install react-native-instantpay-location-enabler
```

## Manual installation
### Android
1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.instantpaylocationenabler.InstantpayLocationEnablerPackage;` to the imports at the top of the file
  - Add `new InstantpayLocationEnablerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-instantpay-location-enabler'
  	project(':react-native-instantpay-location-enabler').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-instantpay-location-enabler/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-instantpay-location-enabler')

## Usage

```js
import RNLocationEnabler from 'react-native-instantpay-location-enabler';

// ...

let res = await RNLocationEnabler.checkLocation();
```

**Note about checkLocation Method**

Possible options values : 

| key                   | Description                                        |  type      | required       |
| --------------------  | -------------------------------------------------- | ---------- | -------------- |
| askDailogPermission   | Request to Open Dialog Permission                  | boolean    | Optional       |

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Created By [Instantpay](https://www.instantpay.in)
