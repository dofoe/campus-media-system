const mockUsers = [
  { id: 1, username: 'admin', name: '系统管理员', dept: '宣传部', deptId: 1, role: 'admin', roleName: '超级管理员', email: 'admin@campus.edu.cn', phone: '13800138000', status: true, dataScope: 'all', createTime: '2024-01-01 00:00:00' },
  { id: 2, username: 'dept_admin1', name: '张主任', dept: '计算机学院', deptId: 2, role: 'dept_admin', roleName: '部门管理员', email: 'zhang@campus.edu.cn', phone: '13900139001', status: true, dataScope: 'dept', createTime: '2024-02-01 10:00:00' },
  { id: 3, username: 'dept_admin2', name: '李主任', dept: '文学院', deptId: 3, role: 'dept_admin', roleName: '部门管理员', email: 'li@campus.edu.cn', phone: '13900139002', status: true, dataScope: 'dept', createTime: '2024-02-02 11:00:00' },
  { id: 4, username: 'user1', name: '王小明', dept: '计算机学院', deptId: 2, role: 'user', roleName: '普通用户', email: 'wang1@campus.edu.cn', phone: '13800138001', status: true, dataScope: 'self', createTime: '2024-03-01 09:00:00' },
  { id: 5, username: 'user2', name: '赵小红', dept: '计算机学院', deptId: 2, role: 'user', roleName: '普通用户', email: 'zhao@campus.edu.cn', phone: '13800138002', status: true, dataScope: 'self', createTime: '2024-03-02 10:00:00' },
  { id: 6, username: 'user3', name: '孙大伟', dept: '文学院', deptId: 3, role: 'user', roleName: '普通用户', email: 'sun@campus.edu.cn', phone: '13800138003', status: true, dataScope: 'self', createTime: '2024-03-03 11:00:00' },
  { id: 7, username: 'user4', name: '周小丽', dept: '商学院', deptId: 4, role: 'user', roleName: '普通用户', email: 'zhou@campus.edu.cn', phone: '13800138004', status: true, dataScope: 'self', createTime: '2024-03-04 12:00:00' },
  { id: 8, username: 'user5', name: '吴小军', dept: '理工学院', deptId: 5, role: 'user', roleName: '普通用户', email: 'wu@campus.edu.cn', phone: '13800138005', status: false, dataScope: 'self', createTime: '2024-03-05 13:00:00' }
]

const mockMediaList = Array.from({ length: 50 }, (_, i) => ({
  id: i + 1,
  fileName: `素材文件_${i + 1}.${['jpg', 'png', 'mp4', 'docx', 'pdf'][i % 5]}`,
  fileType: ['image', 'image', 'video', 'document', 'document'][i % 5],
  fileSize: Math.floor(Math.random() * 50000000) + 100000,
  thumbnailUrl: `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(['campus building', 'student activity', 'graduation ceremony', 'campus landscape', 'sports event'][i % 5])}&image_size=square`,
  category: ['校园活动', '校园风景', '师生风采', '新闻资讯', '教学科研', '会议资料'][i % 6],
  categoryId: (i % 6) + 1,
  tags: ['校园', '宣传', ['活动', '风景', '人物', '新闻', '教学', '会议'][i % 6]],
  aiTags: ['AI识别标签1', 'AI识别标签2', 'AI识别标签3'],
  description: `这是第${i + 1}个素材的描述信息，包含了丰富的内容和详细的说明。`,
  copyrightInfo: '版权所有 © 校园宣传部',
  uploadUser: i % 3 === 0 ? 'admin' : 'user' + (i % 5),
  uploadTime: new Date(Date.now() - i * 86400000).toISOString(),
  downloadCount: Math.floor(Math.random() * 500),
  usageCount: Math.floor(Math.random() * 100),
  status: 'published',
  auditStatus: ['approved', 'pending', 'rejected'][i % 3]
}))

const mockDownloadLogs = Array.from({ length: 30 }, (_, i) => ({
  id: i + 1,
  mediaId: (i % 10) + 1,
  fileName: `素材文件_${(i % 10) + 1}.jpg`,
  userName: ['admin', 'user1', 'user2', 'user3'][i % 4],
  dept: ['宣传部', '计算机学院', '文学院', '商学院'][i % 4],
  downloadTime: new Date(Date.now() - i * 3600000 * 2).toISOString(),
  clientIp: `192.168.1.${i % 255}`
}))

const mockOperationLogs = Array.from({ length: 40 }, (_, i) => ({
  id: i + 1,
  userId: (i % 5) + 1,
  userName: ['admin', 'dept_admin1', 'user1', 'user2', 'user3'][i % 5],
  operation: ['login', 'upload', 'download', 'edit', 'delete', 'audit'][i % 6],
  target: '素材文件_' + ((i % 20) + 1),
  targetId: (i % 20) + 1,
  detail: '执行了' + ['登录', '上传', '下载', '编辑', '删除', '审核'][i % 6] + '操作',
  clientIp: `192.168.1.${i % 255}`,
  createdAt: new Date(Date.now() - i * 7200000).toISOString()
}))

const mockRules = [
  { id: 1, name: '敏感词过滤规则', type: 'keyword', action: 'reject', description: '过滤包含敏感词的素材', status: true, condition: { keywords: ['敏感词1', '敏感词2', '敏感词3'] }, createTime: '2024-01-15 10:00:00' },
  { id: 2, name: '视频大小限制', type: 'file_size', action: 'reject', description: '限制视频文件最大500MB', status: true, condition: { fileType: 'video', maxSize: 524288000 }, createTime: '2024-02-01 14:00:00' },
  { id: 3, name: '图片格式规范', type: 'file_type', action: 'reject', description: '仅允许指定格式的图片', status: true, condition: { allowedTypes: ['jpg', 'jpeg', 'png', 'gif', 'webp'] }, createTime: '2024-02-10 09:00:00' },
  { id: 4, name: '自动分类规则', type: 'auto_category', action: 'auto', description: '根据AI标签自动分类', status: true, condition: { tagToCategory: { '校园活动': 1, '校园风景': 2, '师生风采': 3 } }, createTime: '2024-03-01 11:00:00' },
  { id: 5, name: '版权检查规则', type: 'copyright', action: 'review', description: '疑似版权内容需要人工审核', status: false, condition: { checkKeywords: ['版权', '授权', '转载'] }, createTime: '2024-03-15 16:00:00' }
]

const mockCategories = [
  { id: 1, name: '校园活动', parentId: null, sortOrder: 1, status: true, children: [
    { id: 11, name: '开学典礼', parentId: 1, sortOrder: 1, status: true },
    { id: 12, name: '毕业典礼', parentId: 1, sortOrder: 2, status: true },
    { id: 13, name: '运动会', parentId: 1, sortOrder: 3, status: true }
  ]},
  { id: 2, name: '校园风景', parentId: null, sortOrder: 2, status: true, children: [
    { id: 21, name: '春夏秋冬', parentId: 2, sortOrder: 1, status: true },
    { id: 22, name: '建筑景观', parentId: 2, sortOrder: 2, status: true }
  ]},
  { id: 3, name: '师生风采', parentId: null, sortOrder: 3, status: true },
  { id: 4, name: '新闻资讯', parentId: null, sortOrder: 4, status: true },
  { id: 5, name: '教学科研', parentId: null, sortOrder: 5, status: true },
  { id: 6, name: '会议资料', parentId: null, sortOrder: 6, status: true }
]

const mockDepts = [
  { id: 1, name: '宣传部', parentId: null, sortOrder: 1, status: true },
  { id: 2, name: '计算机学院', parentId: null, sortOrder: 2, status: true },
  { id: 3, name: '文学院', parentId: null, sortOrder: 3, status: true },
  { id: 4, name: '商学院', parentId: null, sortOrder: 4, status: true },
  { id: 5, name: '理工学院', parentId: null, sortOrder: 5, status: true }
]

const mockRoles = [
  { id: 1, name: '超级管理员', code: 'admin', description: '系统超级管理员', dataScope: 'all', status: true },
  { id: 2, name: '部门管理员', code: 'dept_admin', description: '部门管理员', dataScope: 'dept', status: true },
  { id: 3, name: '普通用户', code: 'user', description: '普通用户', dataScope: 'self', status: true }
]

function loginMock(data) {
  if (data.username === 'admin' && data.password === 'admin') {
    return {
      code: 200,
      message: 'success',
      data: {
        token: 'mock-jwt-token-' + Date.now(),
        userInfo: mockUsers[0]
      }
    }
  } else if (data.username === 'user' && data.password === 'user') {
    return {
      code: 200,
      message: 'success',
      data: {
        token: 'mock-jwt-token-user-' + Date.now(),
        userInfo: mockUsers[3]
      }
    }
  }
  return { code: 401, message: '用户名或密码错误', data: null }
}

function searchMediaMock(params) {
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  const keyword = params.keyword || ''
  const category = params.category || ''
  const fileType = params.fileType || ''
  const tag = params.tag || ''

  let filtered = mockMediaList.filter(item => {
    if (keyword && !item.fileName.includes(keyword) && !item.description.includes(keyword)) return false
    if (category && item.category !== category) return false
    if (fileType && item.fileType !== fileType) return false
    if (tag && !item.tags.includes(tag) && !item.aiTags.includes(tag)) return false
    return true
  })

  const total = filtered.length
  const list = filtered.slice((pageNum - 1) * pageSize, pageNum * pageSize)

  return {
    code: 200,
    message: 'success',
    data: { list, total, pageNum, pageSize }
  }
}

function getMediaDetailMock(id) {
  const item = mockMediaList.find(m => m.id === parseInt(id)) || mockMediaList[0]
  return {
    code: 200,
    message: 'success',
    data: {
      ...item,
      storagePath: `/uploads/${item.fileType}/${item.fileName}`,
      md5Hash: 'abc123def456ghi789jkl012mno345pq',
      fileUrl: item.thumbnailUrl,
      versions: [
        { name: '原图', size: item.fileSize, url: item.thumbnailUrl },
        { name: '中图', size: Math.floor(item.fileSize * 0.5), url: item.thumbnailUrl },
        { name: '缩略图', size: Math.floor(item.fileSize * 0.1), url: item.thumbnailUrl }
      ],
      operationLogs: [
        { time: item.uploadTime, user: item.uploadUser, action: '上传', detail: '文件上传完成' },
        { time: new Date(Date.parse(item.uploadTime) + 3600000).toISOString(), user: 'ai_system', action: 'AI处理', detail: 'AI标签识别完成' },
        { time: new Date(Date.parse(item.uploadTime) + 7200000).toISOString(), user: 'admin', action: '审核', detail: '审核通过' }
      ]
    }
  }
}

function getUsersMock(params) {
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  const keyword = params.keyword || ''
  const role = params.role || ''
  const dept = params.dept || ''

  let filtered = mockUsers.filter(u => {
    if (keyword && !u.username.includes(keyword) && !u.name.includes(keyword)) return false
    if (role && u.role !== role) return false
    if (dept && u.dept !== dept) return false
    return true
  })

  return {
    code: 200,
    message: 'success',
    data: {
      list: filtered.slice((pageNum - 1) * pageSize, pageNum * pageSize),
      total: filtered.length,
      pageNum,
      pageSize
    }
  }
}

function getDownloadLogsMock(params) {
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  const keyword = params.keyword || ''

  let filtered = mockDownloadLogs
  if (keyword) {
    filtered = filtered.filter(l => l.fileName.includes(keyword) || l.userName.includes(keyword))
  }

  return {
    code: 200,
    message: 'success',
    data: {
      list: filtered.slice((pageNum - 1) * pageSize, pageNum * pageSize),
      total: filtered.length,
      pageNum,
      pageSize
    }
  }
}

function getOperationLogsMock(params) {
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  const keyword = params.keyword || ''
  const operation = params.operation || ''

  let filtered = mockOperationLogs
  if (keyword) {
    filtered = filtered.filter(l => l.userName.includes(keyword) || l.target.includes(keyword))
  }
  if (operation) {
    filtered = filtered.filter(l => l.operation === operation)
  }

  return {
    code: 200,
    message: 'success',
    data: {
      list: filtered.slice((pageNum - 1) * pageSize, pageNum * pageSize),
      total: filtered.length,
      pageNum,
      pageSize
    }
  }
}

function getRulesMock(params) {
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  return {
    code: 200,
    message: 'success',
    data: {
      list: mockRules,
      total: mockRules.length,
      pageNum,
      pageSize
    }
  }
}

function getPendingAuditMock(params) {
  const list = mockMediaList.filter(m => m.auditStatus === 'pending')
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 20
  return {
    code: 200,
    message: 'success',
    data: {
      list: list.slice((pageNum - 1) * pageSize, pageNum * pageSize),
      total: list.length,
      pageNum,
      pageSize
    }
  }
}

function successMock(data = null) {
  return { code: 200, message: 'success', data }
}

export function getMockResponse(url, method, data, params) {
  let cleanUrl = url.replace('/api/v1', '')

  if (cleanUrl === '/auth/login') return loginMock(data)
  if (cleanUrl === '/auth/menus') return successMock([
    { path: '/home', name: '首页', icon: 'House' },
    { path: '/upload', name: '上传素材', icon: 'UploadFilled' },
    { path: '/dashboard', name: '管理后台', icon: 'Grid', children: [
      { path: '/dashboard', name: '数据概览' },
      { path: '/admin/media', name: '素材管理' },
      { path: '/admin/rules', name: '规则配置' },
      { path: '/admin/users', name: '用户管理' },
      { path: '/admin/audit', name: '审计日志' }
    ]}
  ])

  if (cleanUrl === '/media/search') return searchMediaMock(params)
  if (cleanUrl === '/media/upload/init') return successMock({ uploadId: 'upload_' + Date.now(), chunkSize: 5 * 1024 * 1024 })
  if (cleanUrl === '/media/upload/complete') return successMock({
    id: Math.floor(Math.random() * 1000),
    fileName: '上传的文件.jpg',
    thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus&image_size=square',
    aiTags: ['AI标签1', 'AI标签2', 'AI标签3'],
    category: '未分类'
  })
  if (cleanUrl === '/media/audit/pending') return getPendingAuditMock(params)
  if (cleanUrl === '/media/categories') return successMock(mockCategories)
  if (cleanUrl === '/media/tags') return successMock(['校园', '宣传', '活动', '风景', '人物', '新闻', '教学', '会议', '建筑', '体育', '艺术', '科技'])

  const mediaDetailMatch = cleanUrl.match(/^\/media\/(\d+)$/)
  if (mediaDetailMatch && method === 'get') return getMediaDetailMock(mediaDetailMatch[1])
  if (mediaDetailMatch && method === 'put') return successMock({ ...data, id: parseInt(mediaDetailMatch[1]) })
  if (mediaDetailMatch && method === 'delete') return successMock()

  const mediaDownloadMatch = cleanUrl.match(/^\/media\/(\d+)\/download$/)
  if (mediaDownloadMatch) return successMock({
    downloadUrl: mockMediaList[0]?.thumbnailUrl || '',
    fileName: `素材文件_${mediaDownloadMatch[1]}.zip`
  })

  const auditMatch = cleanUrl.match(/^\/media\/audit\/(\d+)$/)
  if (auditMatch) return successMock()

  if (cleanUrl === '/media/batch') return successMock()

  if (cleanUrl === '/admin/dashboard') return successMock({
    todayUpload: 128,
    todayDownload: 526,
    storageUsed: '128.5 GB',
    totalMedia: 12586,
    uploadTrend: [
      { date: '07-01', upload: 120, download: 500 },
      { date: '07-02', upload: 150, download: 600 },
      { date: '07-03', upload: 100, download: 450 },
      { date: '07-04', upload: 200, download: 800 },
      { date: '07-05', upload: 180, download: 700 },
      { date: '07-06', upload: 140, download: 550 },
      { date: '07-07', upload: 128, download: 526 }
    ],
    categoryDist: [
      { name: '校园活动', count: 3520, percentage: 28 },
      { name: '校园风景', count: 2860, percentage: 23 },
      { name: '师生风采', count: 2450, percentage: 19 },
      { name: '新闻资讯', count: 1890, percentage: 15 },
      { name: '教学科研', count: 1260, percentage: 10 },
      { name: '会议资料', count: 606, percentage: 5 }
    ],
    typeDist: [
      { name: '图片', count: 8950, percentage: 71 },
      { name: '视频', count: 1580, percentage: 13 },
      { name: '文档', count: 2056, percentage: 16 }
    ],
    hotMedia: mockMediaList.slice(0, 5).map((m, i) => ({ ...m, rank: i + 1 })),
    activeDepts: [
      { rank: 1, name: '计算机学院', uploadCount: 320, downloadCount: 1580, contribution: 25 },
      { rank: 2, name: '宣传部', uploadCount: 280, downloadCount: 1420, contribution: 22 },
      { rank: 3, name: '文学院', uploadCount: 250, downloadCount: 1200, contribution: 20 },
      { rank: 4, name: '商学院', uploadCount: 200, downloadCount: 980, contribution: 16 },
      { rank: 5, name: '理工学院', uploadCount: 150, downloadCount: 750, contribution: 12 }
    ]
  })

  if (cleanUrl === '/admin/users') {
    if (method === 'get') return getUsersMock(params)
    if (method === 'post') return successMock({ id: Date.now() })
  }

  const userMatch = cleanUrl.match(/^\/admin\/users\/(\d+)$/)
  if (userMatch) {
    if (method === 'put') return successMock()
    if (method === 'delete') return successMock()
  }

  const resetPwdMatch = cleanUrl.match(/^\/admin\/users\/(\d+)\/password$/)
  if (resetPwdMatch) return successMock()

  if (cleanUrl === '/admin/roles') return successMock(mockRoles)
  if (cleanUrl === '/admin/depts') return successMock(mockDepts)
  if (cleanUrl === '/admin/categories') return successMock(mockCategories)

  if (cleanUrl === '/admin/audit/download') return getDownloadLogsMock(params)
  if (cleanUrl === '/admin/audit/operation') return getOperationLogsMock(params)

  if (cleanUrl === '/admin/rules') {
    if (method === 'get') return getRulesMock(params)
    if (method === 'post') return successMock({ id: Date.now() })
  }

  const ruleMatch = cleanUrl.match(/^\/admin\/rules\/(\d+)$/)
  if (ruleMatch) {
    if (method === 'put') return successMock()
    if (method === 'delete') return successMock()
  }

  if (cleanUrl === '/admin/rules/test') return successMock({ passed: true, matched: [] })

  return { code: 404, message: '接口不存在: ' + cleanUrl, data: null }
}
