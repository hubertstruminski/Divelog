import React from 'react';
import swal from 'sweetalert';
import $ from 'jquery';

class DeleteAttachmentButton extends React.Component {
    constructor(props) {
        super(props);

        this.onImageDeleteClick = this.onImageDeleteClick.bind(this);
    }

    onImageDeleteClick() {
        let fileId = this.props.id;
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/delete/post/file/${fileId}/${jwtToken}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => {
            if(response.status !== 200) {
                swal("Error", "Something goes wrong.", "error");
            } else {
                let counter = this.props.counter;
                $(`.main-post-attachments:eq(${counter})`).find(`#attachment${fileId}`).empty();
                // $(`.post-buttons:eq(${counter})`).find(`#btnDeleteAttachment${this.props.id}`).remove();
                this.props.setDeletedFileForPost(this.props.postId, this.props.id);
                swal("Success", "File was removed successfully.", "success");
            }
        });
    }

    render() {
        return (
            <button 
                id={`btnDeleteAttachment${this.props.id}`}
                className="btn btn-danger"
                onClick={this.onImageDeleteClick}
            >
                DELETE {this.props.type} - {this.props.name}
            </button>
        );
    }
}

export default DeleteAttachmentButton;