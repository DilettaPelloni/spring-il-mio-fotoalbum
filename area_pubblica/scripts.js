const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/",
            photos: [],
            keyword:"",
            imageUrl: "http://localhost:8080/files/image/",
            email: "",
            message: "",
            displaySuccess: false,
            errors: [],
        }//return
    },//data
    computed: {
        emailHasErrors() {
            let flag = false;
            this.errors.forEach(error => {
                if(error.field == "email") {
                    flag = true;
                }
            });
            return flag;
        },
        messageHasErrors() {
            let flag = false;
            this.errors.forEach(error => {
                if(error.field == "message") {
                    flag = true;
                }
            });
            return flag;
        },
        getEmailErrorMessages() {
            let messages = [];
            this.errors.forEach(error => {
                if(error.field == "email") {
                    messages.push(error.defaultMessage);
                }
            });
            return messages
        },
        getMessageErrorMessages() {
            let messages = [];
            this.errors.forEach(error => {
                if(error.field == "message") {
                    messages.push(error.defaultMessage);
                }
            });
            return messages
        },
    },
    methods: {
        async getPhotos() {
            try {
                const response = await axios.get(this.url+"photos");
                this.photos = response.data;
            } catch(error) {
                console.log(error);
            }
        },
        async getFilteredPhotos() {
            try {
                const response = await axios.get(this.url+"photos", {
                                        params: {
                                            keyword: this.keyword
                                        }
                                    });
                this.photos = response.data;
            } catch(error) {
                console.log(error);
            }
        },
        async sendMessage() {
            try {
                const response = await axios.post(this.url+"messages/create", {
                    email: this.email,
                    message: this.message
                  });
                if(response.data.success == true) {
                    this.errors = [];
                    this.displaySuccess = true;
                }
            } catch(error) {
                this.errors = error.response.data.errors;
            }
        }
    },//methods
    created() {
        this.getPhotos();
    }
}).mount('#app');