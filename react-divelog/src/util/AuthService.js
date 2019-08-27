import decode from 'jwt-decode';

export default class AuthService {

    setToken(idToken) {
        localStorage.setItem("JwtToken", idToken);
    }

    getToken() {
        return localStorage.getItem("JwtToken");
    }

    isTokenExpired(token) {
        try {
            const decoded = decode(token);

            if(decoded.exp < Date.now() / 1000) {
                return true;
            } else {
                return false;
            }
        } catch(error) {
            return false;
        }
    }

    loggedIn() {
        const token = this.getToken();
        return !!token && !this.isTokenExpired(token);
    }

    logout() {
        localStorage.removeItem("JwtToken");
    }
}