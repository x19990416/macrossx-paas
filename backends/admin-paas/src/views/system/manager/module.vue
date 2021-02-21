<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.blurry"
        placeholder="用户名"
        size="small"
        style="width: 200px;"
        class="filter-item filter-time-margin"
      />
      <el-button
        v-waves
        class="filter-item"
        type="success"
        size="small"
        icon="el-icon-search"
        style="margin-left: 10px;"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="warning"
        size="small"
        icon="el-icon-refresh"
        @click="resetTemp"
      >
        重置
      </el-button>
    </div>
    <div class="filter-container">
      <el-button v-waves class="filter-item" type="primary" size="small" icon="el-icon-plus" @click="handleCreate">
        新增
      </el-button>
    </div>
    <el-row :gutter="24">
      <el-col :span="18">
        <el-table
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="选择" width="60px" align="center" header-align="center">
            <template slot-scope="scope">
              <el-radio
                v-model="tempRadio"
                :label="scope.$index"
                style="margin-left: 10px;"
                @change.native="getTemplateRow(scope.$index,scope.row)"
              >
                <span /></el-radio>
            </template>

          </el-table-column>
          <el-table-column label="模块名" width="" align="center">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="组织名" width="" align="center">
            <template slot-scope="{row}">
              <span>{{ row.groupId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="标识符" width="" align="center">
            <template slot-scope="{row}">
              <span>{{ row.artifactId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="版本号" width="" align="center">
            <template slot-scope="{row}">
              <span>{{ row.version }}</span>
            </template>
          </el-table-column>
          <el-table-column label="仓库" align="center">
            <template slot-scope="{row}">
              <span>Maven-snap</span>
            </template>
          </el-table-column>
          <el-table-column label="说明" align="center">
            <template slot-scope="{row}">
              <span>{{ row.description?row.description:'-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="{row,$index}">
              <el-button type="primary" size="small" icon="el-icon-edit" @click="handleUpdate(row)" />
              <el-button
                v-if="row.status!='deleted'"
                size="small"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              />
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="listQuery.page"
          :limit.sync="listQuery.limit"
          @pagination="getModule"
        />
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>相关数据库表</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleTableSave">保存</el-button>
          </div>
          <el-tree
            ref="table"
            :data="tables"
            :props="tablesTreeProps"
            show-checkbox
            node-key="name"
            :default-checked-keys="selectedTables"
          />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="模块名" prop="name">
          <el-input v-model="temp.name" :disabled="(dialogStatus==='update' && temp.name)?true:false" />
        </el-form-item>
        <el-form-item label="组织名" prop="groupId">
          <el-input
            v-model="temp.groupId"
            placeholder="请输入组织名"
            :disabled="(dialogStatus==='update' && temp.groupId)?true:false"
          />
        </el-form-item>
        <el-form-item label="标识符" prop="artifactId">
          <el-input
            v-model="temp.artifactId"
            placeholder="请输入标识符"
            :disabled="(dialogStatus==='update' && temp.artifactId)?true:false"
          />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input
            v-model="temp.version"
            placeholder="请输入版本号"
          />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="temp.description" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<style>
  filter-time-margin {
    margin: 0 5px;
  }

</style>
<script>
import { createModule, fetchModule, fetchTables, updateModule } from '@/api/manager'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SystemOverview',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tempRadio: null,
      selectedTables: null,
      sys_type_options: [{ value: 1, label: '后端' }, { value: 2, label: '前端' }],
      selectedSystem: [],
      tables: null,
      tablesTreeProps: {
        children: 'children',
        label: 'name'
      },
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        blurry: undefined
      },
      temp: {
        name: undefined,
        description: undefined,
        groupId: undefined,
        artifactId: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: '新增'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'blue' }],
        groupId: [{ required: true, message: 'group is required', trigger: 'blue' }],
        version: [{ required: true, message: 'version is required', trigger: 'blue' }],
        artifactId: [{ required: true, message: 'artifact is required', trigger: 'blue' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getModule()
    this.getTable()
  },
  methods: {
    getTable() {
      this.listLoading = true
      fetchTables().then(response => {
        this.tables = response
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    getModule() {
      this.listLoading = true
      const data = {
        ...this.listQuery,
        page: this.listQuery.page > 0 ? this.listQuery.page - 1 : 0
      }
      fetchModule(data).then(response => {
        this.list = response.contents
        this.total = response.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getModule()
    },
    resetTemp() {
      this.temp = {
        name: undefined,
        group: undefined,
        description: undefined,
        id: undefined,
        artifact: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createModule(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateModule(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
            this.getModule()
          })
        }
      })
    },
    handleTableSave() {
      if (this.selectedRow) {
        this.selectedRow.tables = this.$refs.table.getCheckedNodes().filter(o => !o.children).map(entity => entity.name)
        updateModule(this.selectedRow).then(() => {
          this.$notify({
            title: 'Success',
            message: 'update Successfully',
            type: 'success',
            duration: 2000
          })
        })
      }
    },
    handleDelete(row, index) {
      deleteUser([row.id]).then(() => {
        this.$notify({
          title: 'Success',
          message: 'Delete Successfully',
          type: 'success',
          duration: 2000
        })
        this.list.splice(index, 1)
      })
    },
    handleClearFilter() {
      this.listQuery = {
        ...this.listQuery,
        name: ''
      }
    },
    getTemplateRow(index, row) {
      this.selectedRow = row
      fetchModule({ id: row.id }).then(response => {
        if (response.contents && response.contents.length === 1) {
          this.selectedTables = response.contents[0].tables
        }
      })
    }
  }
}
</script>
