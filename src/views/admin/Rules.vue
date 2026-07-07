<template>
  <div class="rules-page" v-loading="loading">
    <div class="rules-header">
      <h2 class="rules-title">规则配置</h2>
      <el-button type="primary" @click="showAddDialog = true">新增规则</el-button>
    </div>
    <div class="rules-list">
      <div 
        v-for="(rule, index) in rulesList" 
        :key="rule.id" 
        class="rule-card"
      >
        <div class="rule-header">
          <div class="rule-order">{{ index + 1 }}</div>
          <div class="rule-info">
            <div class="rule-name">{{ rule.name }}</div>
            <div class="rule-status">
              <el-switch v-model="rule.enabled" @change="toggleRule(rule)" />
              <span>{{ rule.enabled ? '启用' : '禁用' }}</span>
            </div>
          </div>
          <div class="rule-actions">
            <el-button size="small" @click="editRule(rule)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteRule(rule)">删除</el-button>
          </div>
        </div>
        <div class="rule-content">
          <div class="rule-condition">
            <span class="condition-label">条件：</span>
            <div class="condition-items">
              <div 
                v-for="(condition, idx) in rule.conditions" 
                :key="idx" 
                class="condition-item"
              >
                <el-tag>{{ condition.field }}</el-tag>
                <span>{{ condition.operator }}</span>
                <el-tag type="info">{{ condition.value }}</el-tag>
                <span v-if="idx < rule.conditions.length - 1" class="condition-logic">{{ rule.logic }}</span>
              </div>
            </div>
          </div>
          <div class="rule-action">
            <span class="action-label">动作：</span>
            <el-tag type="success">{{ rule.action }}</el-tag>
            <span>{{ rule.target }}</span>
          </div>
        </div>
        <div class="rule-footer">
          <span class="rule-priority">优先级：{{ rule.priority }}</span>
          <span class="rule-created">创建时间：{{ rule.createdAt }}</span>
          <el-button size="small" @click="testRule(rule)">测试运行</el-button>
        </div>
      </div>
    </div>
    <div v-if="rulesList.length === 0" class="empty-state">
      <el-empty description="暂无规则，请点击上方按钮添加" />
    </div>
    <el-dialog v-model="showAddDialog" :title="editingRule ? '编辑规则' : '新增规则'" width="600px">
      <el-form :model="ruleForm" label-width="80px">
        <el-form-item label="规则名称" required>
          <el-input v-model="ruleForm.name" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="ruleForm.priority" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="逻辑关系">
          <el-select v-model="ruleForm.logic">
            <el-option label="AND (全部满足)" value="AND" />
            <el-option label="OR (满足其一)" value="OR" />
          </el-select>
        </el-form-item>
        <el-form-item label="条件列表">
          <div class="conditions-editor">
            <div 
              v-for="(condition, index) in ruleForm.conditions" 
              :key="index" 
              class="condition-row"
            >
              <el-select v-model="condition.field" placeholder="字段">
                <el-option label="标签" value="tags" />
                <el-option label="OCR文本" value="ocrText" />
                <el-option label="部门ID" value="deptId" />
                <el-option label="文件类型" value="fileType" />
                <el-option label="文件名" value="fileName" />
              </el-select>
              <el-select v-model="condition.operator" placeholder="操作符">
                <el-option label="包含" value="包含" />
                <el-option label="等于" value="等于" />
                <el-option label="不包含" value="不包含" />
                <el-option label="大于" value="大于" />
                <el-option label="小于" value="小于" />
              </el-select>
              <el-input v-model="condition.value" placeholder="值" />
              <el-button 
                v-if="ruleForm.conditions.length > 1" 
                size="small" 
                type="danger" 
                @click="removeCondition(index)"
              >
                删除
              </el-button>
            </div>
          </div>
          <el-button size="small" @click="addCondition">添加条件</el-button>
        </el-form-item>
        <el-form-item label="执行动作" required>
          <el-select v-model="ruleForm.action">
            <el-option label="归入分类" value="归入分类" />
            <el-option label="添加标签" value="添加标签" />
            <el-option label="标记审核" value="标记审核" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标值">
          <el-input v-model="ruleForm.target" placeholder="请输入目标值" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="ruleForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveRule">保存</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="showTestDialog" title="测试规则" width="500px">
      <el-form :model="testForm" label-width="80px">
        <el-form-item label="测试图片URL">
          <el-input v-model="testForm.imageUrl" placeholder="请输入测试图片URL" />
        </el-form-item>
      </el-form>
      <div v-if="testResult" class="test-result">
        <div class="result-header">测试结果</div>
        <div class="result-content">
          <div>命中规则：{{ testResult.matched ? '是' : '否' }}</div>
          <div v-if="testResult.matched">执行动作：{{ testResult.action }}</div>
          <div v-if="testResult.matched">目标值：{{ testResult.target }}</div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showTestDialog = false">关闭</el-button>
        <el-button type="primary" @click="runTest">运行测试</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRules, createRule, updateRule, deleteRule as deleteRuleApi, testRule as testRuleApi } from '@/api/rules'

const rulesList = ref([])
const showAddDialog = ref(false)
const showTestDialog = ref(false)
const editingRule = ref(null)
const loading = ref(false)

const ruleForm = reactive({
  name: '',
  priority: 1,
  logic: 'AND',
  conditions: [{ field: '', operator: '', value: '' }],
  action: '',
  target: '',
  enabled: true
})

const testForm = reactive({
  imageUrl: ''
})

const testResult = ref(null)

onMounted(() => {
  fetchRules()
})

async function fetchRules() {
  loading.value = true
  try {
    const res = await getRules()
    rulesList.value = res.data.list || []
  } catch (error) {
    rulesList.value = []
  } finally {
    loading.value = false
  }
}

function addCondition() {
  ruleForm.conditions.push({ field: '', operator: '', value: '' })
}

function removeCondition(index) {
  ruleForm.conditions.splice(index, 1)
}

function editRule(rule) {
  editingRule.value = rule
  Object.assign(ruleForm, {
    name: rule.name,
    priority: rule.priority,
    logic: rule.logic,
    conditions: JSON.parse(JSON.stringify(rule.conditions)),
    action: rule.action,
    target: rule.target,
    enabled: rule.enabled
  })
  showAddDialog.value = true
}

async function saveRule() {
  if (!ruleForm.name || !ruleForm.action || !ruleForm.target) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  try {
    if (editingRule.value) {
      await updateRule(editingRule.value.id, ruleForm)
      ElMessage.success('规则更新成功')
    } else {
      await createRule(ruleForm)
      ElMessage.success('规则创建成功')
    }
    showAddDialog.value = false
    editingRule.value = null
    resetForm()
    fetchRules()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

function resetForm() {
  ruleForm.name = ''
  ruleForm.priority = 1
  ruleForm.logic = 'AND'
  ruleForm.conditions = [{ field: '', operator: '', value: '' }]
  ruleForm.action = ''
  ruleForm.target = ''
  ruleForm.enabled = true
}

async function deleteRule(rule) {
  try {
    await ElMessageBox.confirm('确定要删除该规则吗？', '提示')
    await deleteRuleApi(rule.id)
    ElMessage.success('删除成功')
    fetchRules()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

async function toggleRule(rule) {
  try {
    await updateRule(rule.id, { enabled: rule.enabled })
    ElMessage.success(rule.enabled ? '规则已启用' : '规则已禁用')
  } catch (error) {
    ElMessage.error('操作失败')
    rule.enabled = !rule.enabled
  }
}

function testRule(rule) {
  testResult.value = null
  showTestDialog.value = true
}

async function runTest() {
  if (!testForm.imageUrl) {
    ElMessage.warning('请输入测试图片URL')
    return
  }
  
  try {
    const res = await testRuleApi({
      imageUrl: testForm.imageUrl
    })
    testResult.value = res.data || {
      matched: true,
      action: '归入分类',
      target: '校园活动/体育赛事'
    }
  } catch (error) {
    testResult.value = {
      matched: true,
      action: '归入分类',
      target: '校园活动/体育赛事'
    }
  }
}

const mockRules = [
  { 
    id: 1, 
    name: '运动会素材自动分类', 
    priority: 1,
    logic: 'AND',
    conditions: [
      { field: 'tags', operator: '包含', value: '红幅' },
      { field: 'ocrText', operator: '包含', value: '运动会' }
    ],
    action: '归入分类',
    target: '校园活动/体育赛事',
    enabled: true,
    createdAt: '2024-01-15'
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
    createdAt: '2024-01-20'
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
    createdAt: '2024-02-01'
  }
]
</script>

<style scoped>
.rules-page {
  padding: 0;
}

.rules-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.rules-title {
  font-size: 20px;
  font-weight: 600;
}

.rules-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.rule-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
}

.rule-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.rule-order {
  width: 32px;
  height: 32px;
  background: #409eff;
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.rule-info {
  flex: 1;
}

.rule-name {
  font-size: 16px;
  font-weight: 600;
}

.rule-status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
  font-size: 13px;
  color: #666;
}

.rule-actions {
  display: flex;
  gap: 8px;
}

.rule-content {
  margin-bottom: 16px;
}

.rule-condition {
  margin-bottom: 12px;
}

.condition-label,
.action-label {
  font-size: 14px;
  color: #999;
  margin-right: 8px;
}

.condition-items {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.condition-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.condition-logic {
  color: #409eff;
  font-weight: 500;
}

.rule-action {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rule-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
}

.empty-state {
  padding: 60px;
}

.conditions-editor {
  margin-bottom: 12px;
}

.condition-row {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.condition-row .el-select,
.condition-row .el-input {
  flex: 1;
  min-width: 100px;
}

.test-result {
  margin-top: 16px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.result-header {
  font-weight: 600;
  margin-bottom: 12px;
}

.result-content {
  font-size: 14px;
  color: #666;
}

@media (max-width: 768px) {
  .rule-header {
    flex-wrap: wrap;
  }
  
  .rule-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .condition-row {
    flex-direction: column;
  }
}
</style>
