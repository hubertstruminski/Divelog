import { fakeAuth } from "../../util/fakeAuth";
import HeaderIn from "./HeaderIn";
import HeaderOut from "./HeaderOut";
import { withRouter } from 'react-router-dom';
import React from 'react';

const Header = withRouter(({ history }) => (
    fakeAuth.isAuthenticated ? (
        <HeaderIn />
    ) : (
        <HeaderOut />
    )
));

export default Header;