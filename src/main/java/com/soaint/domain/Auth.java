package com.soaint.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Auth {
        private String username;
        private String password;
        private String accessToken;
        private int ttl;
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
        private String date;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }
