import React from 'react';
import { withRouter } from 'react-router';

class Trend extends React.Component {
    constructor() {
        super();
        this.onClick = this.onClick.bind(this);
    }

    onClick(e) {
        this.props.history.replace(`/twitter/explore/${this.props.name}`);
    }

    render() {
        return (
            <div 
                className="twitter-trend-container"
                onClick={this.onClick}
            >
                Trending in {this.props.countryName}
                <br />
                <span style={{ color: 'black', fontWeight: '700' }}>{this.props.name}</span>
                <br />
                {this.props.tweetVolume}K Tweets
            </div>
        );
    }
}

export default withRouter(Trend)