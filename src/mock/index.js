const mockUsers = [
  { id: 1, username: 'admin', name: '系统管理员', dept: '宣传部', deptId: 1, role: 'admin', roleName: '超级管理员', email: 'admin@campus.edu.cn', phone: '13800138000', status: true, dataScope: 'all', createdAt: '2024-01-01 00:00:00', createTime: '2024-01-01 00:00:00' },
  { id: 2, username: 'dept_admin1', name: '张主任', dept: '教务处', deptId: 2, role: 'dept_admin', roleName: '部门管理员', email: 'zhang@campus.edu.cn', phone: '13900139001', status: true, dataScope: 'dept', createdAt: '2024-02-01 10:00:00', createTime: '2024-02-01 10:00:00' },
  { id: 3, username: 'dept_admin2', name: '李主任', dept: '学生处', deptId: 3, role: 'dept_admin', roleName: '部门管理员', email: 'li@campus.edu.cn', phone: '13900139002', status: true, dataScope: 'dept', createdAt: '2024-02-02 11:00:00', createTime: '2024-02-02 11:00:00' },
  { id: 4, username: 'user1', name: '王小明', dept: '计算机学院', deptId: 4, role: 'user', roleName: '普通用户', email: 'wang1@campus.edu.cn', phone: '13800138001', status: true, dataScope: 'self', createdAt: '2024-03-01 09:00:00', createTime: '2024-03-01 09:00:00' },
  { id: 5, username: 'user2', name: '赵小红', dept: '计算机学院', deptId: 4, role: 'user', roleName: '普通用户', email: 'zhao@campus.edu.cn', phone: '13800138002', status: true, dataScope: 'self', createdAt: '2024-03-02 10:00:00', createTime: '2024-03-02 10:00:00' },
  { id: 6, username: 'user3', name: '孙大伟', dept: '文学院', deptId: 5, role: 'user', roleName: '普通用户', email: 'sun@campus.edu.cn', phone: '13800138003', status: true, dataScope: 'self', createdAt: '2024-03-03 11:00:00', createTime: '2024-03-03 11:00:00' },
  { id: 7, username: 'user4', name: '周小丽', dept: '商学院', deptId: 6, role: 'user', roleName: '普通用户', email: 'zhou@campus.edu.cn', phone: '13800138004', status: true, dataScope: 'self', createdAt: '2024-03-04 12:00:00', createTime: '2024-03-04 12:00:00' },
  { id: 8, username: 'user5', name: '吴小军', dept: '理工学院', deptId: 7, role: 'user', roleName: '普通用户', email: 'wu@campus.edu.cn', phone: '13800138005', status: false, dataScope: 'self', createdAt: '2024-03-05 13:00:00', createTime: '2024-03-05 13:00:00' }
]

const categories = ['校园活动', '教学教务', '荣誉表彰', '校园风景', '师生风采']
const categoryTags = {
  '校园活动': ['运动会', '开幕式', '活动', '学生'],
  '教学教务': ['实验室', '科研', '教学', '教务'],
  '荣誉表彰': ['奖学金', '表彰', '荣誉', '颁奖'],
  '校园风景': ['樱花', '风景', '校园', '春天'],
  '师生风采': ['人物', '风采', '师生', '合影']
}

const mockMediaList = Array.from({ length: 50 }, (_, i) => {
  const category = categories[i % categories.length]
  const ext = ['jpg', 'png', 'mp4', 'docx', 'pdf'][i % 5]
  const type = ['image', 'image', 'video', 'document', 'document'][i % 5]
  return {
    id: i + 1,
    fileName: `素材文件_${i + 1}.${ext}`,
    type,
    fileType: type,
    fileSize: Math.floor(Math.random() * 50000000) + 100000,
    thumbnailUrl: `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(['campus building', 'student activity', 'graduation ceremony', 'campus landscape', 'sports event'][i % 5])}&image_size=square`,
    category,
    categoryId: (i % categories.length) + 1,
    tags: ['校园', '宣传', ...categoryTags[category]],
    aiTags: ['AI识别标签1', 'AI识别标签2', 'AI识别标签3'],
    description: `这是第${i + 1}个素材的描述信息，包含了丰富的内容和详细的说明。`,
    copyrightInfo: '版权所有 © 校园宣传部',
    uploader: i % 3 === 0 ? 'admin' : 'user' + (i % 5),
    uploadUser: i % 3 === 0 ? 'admin' : 'user' + (i % 5),
    uploadTime: new Date(Date.now() - i * 86400000).toISOString(),
    downloadCount: Math.floor(Math.random() * 500),
    usageCount: Math.floor(Math.random() * 100),
    status: 'published',
    auditStatus: ['approved', 'pending', 'rejected'][i % 3]
  }
})

const mockDownloadLogs = Array.from({ length: 30 }, (_, i) => ({
  id: i + 1,
  mediaId: (i % 10) + 1,
  fileName: `素材文件_${(i % 10) + 1}.jpg`,
  mediaName: `素材文件_${(i % 10) + 1}.jpg`,
  userName: ['admin', 'user1', 'user2', 'user3'][i % 4],
  dept: ['宣传部', '计算机学院', '文学院', '商学院'][i % 4],
  downloadTime: new Date(Date.now() - i * 3600000 * 2).toISOString(),
  clientIp: `192.168.1.${i % 255}`,
  ip: `192.168.1.${i % 255}`,
  userAgent: ['Mozilla/5.0 Windows', 'Mozilla/5.0 MacOS', 'Mozilla/5.0 Android', 'Mozilla/5.0 iOS'][i % 4]
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
  ip: `192.168.1.${i % 255}`,
  createdAt: new Date(Date.now() - i * 7200000).toISOString(),
  operationTime: new Date(Date.now() - i * 7200000).toISOString()
}))

const mockRules = [
  {
    id: 1,
    name: '运动会素材自动分类',
    priority: 1,
    logic: 'AND',
    conditions: [
      { field: 'tags', operator: '包含', value: '运动会' },
      { field: 'ocrText', operator: '包含', value: '运动会' }
    ],
    action: '归入分类',
    target: '校园活动',
    enabled: true,
    createdAt: '2024-01-15 10:00:00'
  },
  {
    id: 2,
    name: '奖学金素材自动分类',
    priority: 2,
    logic: 'OR',
    conditions: [
      { field: 'tags', operator: '包含', value: '奖学金' },
      { field: 'ocrText', operator: '包含', value: '表彰' }
    ],
    action: '归入分类',
    target: '荣誉表彰',
    enabled: true,
    createdAt: '2024-01-20 14:00:00'
  },
  {
    id: 3,
    name: '校园风景标签',
    priority: 3,
    logic: 'AND',
    conditions: [
      { field: 'tags', operator: '包含', value: '风景' },
      { field: 'fileType', operator: '等于', value: 'image' }
    ],
    action: '添加标签',
    target: '校园风景',
    enabled: false,
    createdAt: '2024-02-01 09:00:00'
  },
  {
    id: 4,
    name: '敏感内容人工审核',
    priority: 4,
    logic: 'OR',
    conditions: [
      { field: 'tags', operator: '包含', value: '敏感' },
      { field: 'ocrText', operator: '包含', value: '敏感词' }
    ],
    action: '标记审核',
    target: '人工复核',
    enabled: true,
    createdAt: '2024-03-01 11:00:00'
  }
]

const mockCategories = [
  { id: 1, name: '校园活动', parentId: null, sortOrder: 1, status: true, children: [
    { id: 11, name: '开学典礼', parentId: 1, sortOrder: 1, status: true },
    { id: 12, name: '毕业典礼', parentId: 1, sortOrder: 2, status: true },
    { id: 13, name: '运动会', parentId: 1, sortOrder: 3, status: true }
  ]},
  { id: 2, name: '教学教务', parentId: null, sortOrder: 2, status: true, children: [
    { id: 21, name: '实验室', parentId: 2, sortOrder: 1, status: true },
    { id: 22, name: '科研成果', parentId: 2, sortOrder: 2, status: true }
  ]},
  { id: 3, name: '荣誉表彰', parentId: null, sortOrder: 3, status: true, children: [
    { id: 31, name: '奖学金', parentId: 3, sortOrder: 1, status: true },
    { id: 32, name: '优秀教师', parentId: 3, sortOrder: 2, status: true }
  ]},
  { id: 4, name: '校园风景', parentId: null, sortOrder: 4, status: true, children: [
    { id: 41, name: '春夏秋冬', parentId: 4, sortOrder: 1, status: true },
    { id: 42, name: '建筑景观', parentId: 4, sortOrder: 2, status: true }
  ]},
  { id: 5, name: '师生风采', parentId: null, sortOrder: 5, status: true }
]

const mockDepts = [
  { id: 1, name: '宣传部', parentId: null, sortOrder: 1, status: true },
  { id: 2, name: '教务处', parentId: null, sortOrder: 2, status: true },
  { id: 3, name: '学生处', parentId: null, sortOrder: 3, status: true },
  { id: 4, name: '计算机学院', parentId: null, sortOrder: 4, status: true },
  { id: 5, name: '文学院', parentId: null, sortOrder: 5, status: true },
  { id: 6, name: '商学院', parentId: null, sortOrder: 6, status: true },
  { id: 7, name: '理工学院', parentId: null, sortOrder: 7, status: true }
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
  const categoryId = params.categoryId || ''
  const fileType = params.fileType || params.format || ''
  const tag = params.tag || ''

  let filtered = mockMediaList.filter(item => {
    if (keyword && !item.fileName.includes(keyword) && !item.description.includes(keyword)) return false
    if (category && item.category !== category) return false
    if (categoryId && item.categoryId !== parseInt(categoryId)) return false
    if (fileType && item.type !== fileType && item.fileType !== fileType) return false
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
      type: item.type || 'image',
      url: item.thumbnailUrl,
      fileUrl: item.thumbnailUrl,
      uploader: item.uploader || item.uploadUser,
      storagePath: `/uploads/${item.fileType}/${item.fileName}`,
      md5Hash: 'abc123def456ghi789jkl012mno345pq',
      versions: [
        { name: '原图', size: item.fileSize, url: item.thumbnailUrl },
        { name: '中图', size: Math.floor(item.fileSize * 0.5), url: item.thumbnailUrl },
        { name: '缩略图', size: Math.floor(item.fileSize * 0.1), url: item.thumbnailUrl }
      ],
      exif: {
        captureTime: item.uploadTime,
        cameraModel: 'Canon EOS R6',
        width: 1920,
        height: 1080,
        gps: '30.5728° N, 104.0668° E'
      },
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
  const list = mockMediaList
    .filter(m => m.auditStatus === 'pending')
    .map((m, i) => ({
      ...m,
      riskLevel: ['高', '中', '低'][i % 3],
      reason: [
        'AI识别到疑似违规内容',
        '低置信度识别',
        'OCR文本不清晰',
        '疑似版权内容',
        '需要人工复核'
      ][i % 5]
    }))
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
    stats: {
      todayUpload: 128,
      todayDownload: 526,
      storageUsed: '128.5 GB',
      totalMedia: 12586
    },
    uploadTrend: [
      { label: '周一', count: 120, value: 60 },
      { label: '周二', count: 150, value: 75 },
      { label: '周三', count: 100, value: 50 },
      { label: '周四', count: 200, value: 100 },
      { label: '周五', count: 180, value: 90 },
      { label: '周六', count: 140, value: 70 },
      { label: '周日', count: 128, value: 64 }
    ],
    categoryDist: [
      { name: '校园活动', count: 3520, percentage: 28, color: '#409eff' },
      { name: '教学教务', count: 2860, percentage: 23, color: '#67c23a' },
      { name: '荣誉表彰', count: 2450, percentage: 19, color: '#e6a23c' },
      { name: '校园风景', count: 1890, percentage: 15, color: '#f56c6c' },
      { name: '师生风采', count: 1260, percentage: 10, color: '#909399' }
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
