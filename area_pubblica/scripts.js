const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/photos",
            photos: [],
            keyword:"",
        }//return
    },//data
    methods: {
        async getPhotos() {
            try {
                const response = await axios.get(this.url);
                this.photos = response.data;
            } catch(error) {
                console.log(error);
            }
        },
        async getFilteredPhotos() {
            try {
                const response = await axios.get(this.url, {
                                        params: {
                                            keyword: this.keyword
                                        }
                                    });
                this.photos = response.data;
            } catch(error) {
                console.log(error);
            }
        }
    },//methods
    created() {
        this.getPhotos();
    }
}).mount('#app');