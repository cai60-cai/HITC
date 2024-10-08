import defaultSettings from '@/settings'

const title = defaultSettings.title || '工大圈子运营中台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
