import http from '@/http/httpRequest.js'

export function getMenus() {
    return http({
        url: '/menu/getMenus',
        method: 'get'
    })
}