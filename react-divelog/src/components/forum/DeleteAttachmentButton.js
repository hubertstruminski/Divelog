import React from 'react';

class DeleteAttachmentButton extends React.Component {
    constructor(props) {
        super(props);

        this.onImageDeleteClick = this.onImageDeleteClick.bind(this);
    }

    onImageDeleteClick() {
        let id = this.props.id;
    }

    render() {
        return (
            <button 
                className="btn btn-danger"
                onClick={this.onImageDeleteClick}
            >
                DELETE {this.props.type} - {this.props.name}
            </button>
        );
    }
}

export default DeleteAttachmentButton;