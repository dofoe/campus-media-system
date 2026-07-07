import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const token = ref(localStorage.getItem('token') || '')
  const menus = ref([])

  function setUserInfo(info) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setMenus(m) {
    menus.value = m
  }

  function logout() {
    userInfo.value = {}
    token.value = ''
    menus.value = []
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  return {
    userInfo,
    token,
    menus,
    setUserInfo,
    setToken,
    setMenus,
    logout
  }
})
