const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/",
            photos: [],
            keyword:"",
            imageUrl: "http://localhost:8080/files/image/",
            email: "",
            message: ""
        }//return
    },//data
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
                console.log(response);
            } catch(error) {
                console.log(error);
            }
        }
    },//methods
    created() {
        this.getPhotos();
    }
}).mount('#app');