import decode from 'jwt-decode';
import Cookies from 'universal-cookie';

export default class AuthService {

    setToken(idToken) {
        localStorage.setItem("JwtToken", idToken);
    }

    getToken() {
        console.log("localStorage facebookJwtToken -> getToken()");
        return localStorage.getItem("JwtToken");
    }

    getTwitterToken() {
        let cookie = new Cookies();
        console.log("cookie twitterJwtToken -> getTwitterToken()");
        console.log(cookie.get("twitterJwtToken"));
        return cookie.get("twitterJwtToken");
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

    getRightSocialToken() {
        let jwtToken = null;

        if(this.getTwitterToken() !== null) {
            jwtToken = this.getTwitterToken();
        }
        if(this.getToken() !== null) {
            jwtToken = this.getToken();
        }
        return jwtToken;
    }

    loggedIn() {
        let facebookJwtToken = this.getToken();
        let twitterJwtToken = this.getTwitterToken();

        if(facebookJwtToken !== null) {
            const token = this.getToken();
            return !!token && !this.isTokenExpired(token);
        }

        if(twitterJwtToken !== null) {
            const twitterToken = this.getTwitterToken();
            console.log("twitterJwtToken != null -> inside");
            console.log(!!twitterToken && !this.isTokenExpired(twitterToken));
            return !!twitterToken && !this.isTokenExpired(twitterToken);
        }
        return false;
    }

    logout() {
        localStorage.removeItem("JwtToken");
    }

    logoutTwitter() {
        let cookie = new Cookies();
        cookie.remove("twitterJwtToken");
    }
}