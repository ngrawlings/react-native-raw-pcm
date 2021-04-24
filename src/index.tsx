import { NativeModules, NativeEventEmitter } from 'react-native';
const EventEmitter = new NativeEventEmitter(NativeModules.RawPcm);

export default {
  record:() => NativeModules.RawPcm.record(),
  stop:() => NativeModules.RawPcm.start(),
  playback: (data:string) => NativeModules.RawPcm.playback(data),
  on:(event:string, callback:any) => {
    EventEmitter.removeAllListeners(event);
    return EventEmitter.addListener(event, callback);
  }
};

