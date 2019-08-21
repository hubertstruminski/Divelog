export const fakeAuth = {
    isAuthenticated: false,

    authenticate(cb) {
        this.isAuthenticated = true;
        setTimeout(cb, 100);
    },

    signout() {
        this.isAuthenticated = false;
    }
}