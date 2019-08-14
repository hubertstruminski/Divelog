import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';
import FB from 'fb';
import FacebookLogin from 'react-facebook-login';

class LogIn extends React.Component {
    // constructor(props) {
    //     super(props);

    //     this.handleClick = this.handleClick.bind(this);
    // }
    // componentDidMount() {
    //     window.fbAsyncInit = function() {
    //         FB.init({
    //             appId      : '455695445269575',
    //             cookie     : true,
    //             xfbml      : true,
    //             version    : 'v2.8'
    //         });
      
    //         FB.getLoginStatus(function(response) {
    //             this.statusChangeCallback(response);
    //         }.bind(this));
    //     }.bind(this);

    //     (function(d, s, id) {
    //         var js, fjs = d.getElementsByTagName(s)[0];
    //         if (d.getElementById(id)) return;
    //         js = d.createElement(s); js.id = id;
    //         js.src = "//connect.facebook.net/en_US/all.js";
    //         fjs.parentNode.insertBefore(js, fjs);
    //     }(document, 'script', 'facebook-jssdk'));
    //   }
      
    // statusChangeCallback(response) {
    //     if (response.status === 'connected') {
    //         console.log("connected");
    //         console.log(response);
    //     } else if (response.status === 'not_authorized') {
    //         console.log("not_authorized");
    //         console.log(response);
    //     } else {
    //         console.log("else");
    //         console.log(response);
    //     }
    // }
      
    // checkLoginState() {
    //     FB.getLoginStatus(function(response) {
    //         this.statusChangeCallback(response);
    //     }.bind(this), true);
    // }
      
    // handleClick() {
    //     FB.login(this.checkLoginState());
    // }

    responseFacebook(response) {
        console.log(response["accessToken"]);
        console.log("response 1: ");
        console.log(response);

        FB.setAccessToken(response["accessToken"]);

        FB.api('me', { fields: 'id,name,email,first_name,location,age_range,birthday,gender', access_token: response["accessToken"] }, function (response2) {
            console.log("response 2: ");
            console.log(response2);
        });

        console.log("--------------FEED----------------");

        FB.api('me/feed', 'GET', {}, function (response) {
            console.log(response);
        });

        // window.location.href = "http://localhost:3000/user";
    }

    render() {
        return(
            <div className="login-container">
                <div className="login-form-container form-container-shadow form-size">
                    <div>
                        <span className="logo-text">
                            {this.props.t("login.login")}
                        </span>
                        <br />
                        <img src={logo} alt="divelog" className="logo-space logo" />
                        <br />
                        <span className="logo-text">Divelog</span>
                        <br />
                        <br />
                        {/* <button 
                            type="button" 
                            className="btn btn-lg btn-fb" 
                            style={{color: 'white'}}
                            onClick={this.handleClick}
                        >
                            <i class="fab fa-facebook-f pr-1"></i> 
                            {this.props.t("login.facebook-btn")}
                        </button> */}
                        <FacebookLogin
                            appId="828826390847961"
                            autoLoad={true}
                            callback={this.responseFacebook} 
                            cookie={true}
                            fields="id,name,email,picture"
                            scope="public_profile,user_friends"
                        />
                    </div>
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(LogIn);