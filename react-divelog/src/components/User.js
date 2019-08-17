import React from 'react';
import FB from 'fb';
import axios from 'axios';

class User extends React.Component {
    constructor() {
        super();
        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();

        fetch("/user")
        .then(response => {
            console.log("Done fetch");
        });
    }

    render() {
        return (
            <div>
                <form onSubmit={this.onSubmit}>
                    <button>
                        Request
                    </button>
                </form>
            </div>
        );
    }
}

export default User;