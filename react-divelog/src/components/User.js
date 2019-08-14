import React from 'react';
import FB from 'fb';

class User extends React.Component {
    componentDidMount() {
        FB.api('/me', function(response) {
            console.log('Good to see you, ' + response.name + '.');
            console.log(response);
          });
    }

    render() {
        return (
            <div></div>
        );
    }
}

export default User;