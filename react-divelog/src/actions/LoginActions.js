import axios from 'axios';
import { LOGIN } from './types';

export const login = (history) => async dispatch => {
    const response = await axios.get("http://localhost:8080/login/facebook");
    dispatch({
        type: LOGIN,
        payload: response.data
    })
    history.push("/dashboard");
}