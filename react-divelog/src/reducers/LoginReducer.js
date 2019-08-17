import { LOGIN } from '../actions/types';

const initialState = {
    loginObject: {}
}

export default function(state = initialState, action) {
    switch(action.type) {
        case LOGIN:
            return {
                ...state,
                loginObject: action.payload
            }
        default:
            return state;
    }
}