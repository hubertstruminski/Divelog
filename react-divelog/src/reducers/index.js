import { combineReducers } from 'redux';
import loginReducer from './LoginReducer';

export default combineReducers({
    loginObject: loginReducer
});