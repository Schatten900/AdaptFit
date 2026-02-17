import Toast from 'react-native-toast-message';
import { toastConfig } from '~/components/CustomToast';

export function GlobalToast() {
  return <Toast config={toastConfig}/>;
}
