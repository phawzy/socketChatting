/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataclass;

import java.io.Serializable;

/**
 *
 * @author phawzy
 */
public class UserStatus implements Serializable {

        public String username;
        public String status;

        public UserStatus() {
            username = "";
            status = "";
        }

    }