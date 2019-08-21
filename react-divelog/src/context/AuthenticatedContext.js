export let isAuthenticated = false;

export const changeAuthentication = () => {
    isAuthenticated = true;
};

export let accessToken = '';

export const changeAccessToken = (new_token) => {
    accessToken = new_token;
};