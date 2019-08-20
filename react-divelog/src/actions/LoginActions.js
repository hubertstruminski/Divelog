import axios from 'axios';
import { LOGIN } from './types';

export const login = (loginRequest) => async dispatch => {
    const response = await axios.post("/signin", loginRequest);
    dispatch({
        type: LOGIN,
        payload: response.data
    })
}