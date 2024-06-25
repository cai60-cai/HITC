import axios from 'axios';
import { URL_PREFIX } from "@/api/config";

export function followUser(userId, followId) {
    return axios.post(`${URL_PREFIX}/api/follow/follow`, {
        userId,
        followId
    });
}
