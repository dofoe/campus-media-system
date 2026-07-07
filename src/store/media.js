import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMediaStore = defineStore('media', () => {
  const searchKeyword = ref('')
  const selectedCategory = ref(null)
  const searchHistory = ref(JSON.parse(localStorage.getItem('searchHistory') || '[]'))

  function setSearchKeyword(keyword) {
    searchKeyword.value = keyword
  }

  function setSelectedCategory(category) {
    selectedCategory.value = category
  }

  function addSearchHistory(keyword) {
    if (!keyword.trim()) return
    const index = searchHistory.value.indexOf(keyword)
    if (index > -1) {
      searchHistory.value.splice(index, 1)
    }
    searchHistory.value.unshift(keyword)
    if (searchHistory.value.length > 10) {
      searchHistory.value.pop()
    }
    localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
  }

  function clearSearchHistory() {
    searchHistory.value = []
    localStorage.removeItem('searchHistory')
  }

  return {
    searchKeyword,
    selectedCategory,
    searchHistory,
    setSearchKeyword,
    setSelectedCategory,
    addSearchHistory,
    clearSearchHistory
  }
})
