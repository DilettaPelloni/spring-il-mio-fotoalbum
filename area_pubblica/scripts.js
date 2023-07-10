const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/", //url per richieste API
            imageUrl: "http://localhost:8080/files/image/", //url per immagini
            photos: [], //lista di foto da mostrare
            keyword:"", //chiave di ricerca per filtrare foto
            email: "", //contenuto del campo email del form per inviare messaggi
            message: "", //contenuto del campo message del form per inviare messaggi
            displaySuccess: false, //mostra messaggio di successo nel form per inviare messaggi 
            errors: [], //elenco degli errori nella compilazione del form
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
                    this.email = "";
                    this.message = "";
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