import React from 'react';
import swal from 'sweetalert';
import $ from 'jquery';
import { withTranslation } from 'react-i18next';
import { withRouter } from 'react-router';

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
                swal(this.props.t("error-500.title"), this.props.t("error-500.message"), "error");
            } else {
                let counter = this.props.counter;
                $(`.main-post-attachments:eq(${counter})`).find(`#attachment${fileId}`).empty();
                this.props.setDeletedFileForPost(this.props.postId, this.props.id);
                swal(this.props.t("news.fileUpload.title"), this.props.t("news.fileUpload.message"), "success");
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
                {this.props.t("forum.deleteAttachmentButton")} {this.props.type} - {this.props.name}
            </button>
        );
    }
}

export default withTranslation("common")(withRouter(DeleteAttachmentButton));