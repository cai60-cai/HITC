import axios from 'axios';
import { URL_PREFIX } from "@/api/config";

export function sendMessage(senderId, receiverId, content) {
    return axios.post(`${URL_PREFIX}/api/message`, {
        senderId,
        receiverId,
        content
    });
}
