import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-instantpay-location-enabler' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const InstantpayLocationEnabler = NativeModules.InstantpayLocationEnabler
  ? NativeModules.InstantpayLocationEnabler
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const RNLocationEnabler = (Platform.OS === "ios") ? null : {

    checkLocation: (options={}) => {

        let params = null;

        if(Object.keys(options).length > 0){
            params = JSON.stringify(options);
        }

        return InstantpayLocationEnabler.checkLocation(params);
    }
}


export default RNLocationEnabler;
