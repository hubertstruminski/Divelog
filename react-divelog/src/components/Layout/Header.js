import { fakeAuth } from "../../util/fakeAuth";
import HeaderIn from "./HeaderIn";
import HeaderOut from "./HeaderOut";
import { withRouter } from 'react-router-dom';
import React from 'react';
import AuthService from "../../util/AuthService";

const Auth = new AuthService();
console.log(Auth.loggedIn());

const Header = withRouter(({ history }) => (
    Auth.loggedIn() ? (
        <HeaderIn />
    ) : (
        <HeaderOut />
    )
));

// class Header extends React.Component {
//     render() {
//         let result;
//         if(Auth.loggedIn()) {
//             result = <HeaderIn />;
//         } 
//         if(!Auth.loggedIn()) {
//             result = <HeaderOut />;
//         }
//         return result;
//     }
// }

export default Header;