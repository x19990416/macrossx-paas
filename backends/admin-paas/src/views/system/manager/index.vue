<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.blurry"
        placeholder="系统名"
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
      <el-col :span="16">
        <el-table
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="选择" width="60px" align="center" header-align="center">
            <template slot-scope="scope">
              <el-radio
                v-model="tempRadio"
                :label="scope.$index"
                style="margin-left: 10px;"
                @change.native="getTemplateRow(scope.$index,scope.row)"
              >
                <span/></el-radio>
            </template>

          </el-table-column>
          <el-table-column label="系统名" width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="缩写" width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.abbr }}</span>
            </template>
          </el-table-column>
          <el-table-column label="系统类型" width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ formatType(row.type) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数据源" width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.dataSource }}</span>
            </template>
          </el-table-column>
          <el-table-column label="说明" align="center">
            <template slot-scope="{row}">
              <span>{{ row.description?row.description:'-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="{row,$index}">
              <el-button type="primary" size="small" icon="el-icon-edit" @click="handleUpdate(row)"/>
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
          @pagination="getSystem"
        />
      </el-col>
      <el-col :span="8">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>模块</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleModuleSave">保存</el-button>
          </div>
          <el-tree
            :data="modules"
            node-key="id"
            ref="module"
            :props="moduleTreeProps"
            show-checkbox
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
        <el-form-item label="系统名" prop="name">
          <el-input v-model="temp.name" :disabled="(dialogStatus==='update' && temp.name)?true:false"/>
        </el-form-item>
        <el-form-item label="缩写" prop="abbr">
          <el-input
            v-model="temp.abbr"
            maxlength="10"
            minlength="3"
            show-word-limit
            :disabled="(dialogStatus==='update' && temp.abbr)?true:false"
          />
        </el-form-item>
        <el-form-item label="系统类型" prop="type">
          <el-select v-model="temp.type" placeholder="请选择"
                     :disabled="(dialogStatus==='update' && temp.type)?true:false">
            <el-option
              v-for="item in sys_type_options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源" prop="dataSource">
          <el-input v-model="temp.dataSource"/>
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="temp.description" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请输入"/>
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
        <el-table-column prop="key" label="Channel"/>
        <el-table-column prop="pv" label="Pv"/>
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
  import {fetchPv} from '@/api/article'
  import {createSystem, fetchModule, fetchSystem, updateSystem} from '@/api/manager'
  import waves from '@/directive/waves' // waves directive
  import {parseTime} from '@/utils'
  import Pagination from '@/components/Pagination' // secondary package based on el-pagination

  export default {
    name: 'SystemOverview',
    components: {Pagination},
    directives: {waves},
    data() {
      return {
        selectedRow: null,
        tempRadio: false,
        sys_type_options: [{value: 1, label: '后端'}, {value: 2, label: '前端'}],
        selectedSystem: [],
        modules: null,
        moduleTreeProps: {
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
          abbr: undefined,
          type: undefined,
          dataSource: undefined
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
          name: [{required: true, message: 'name is required', trigger: 'blue'}],
          abbr: [{required: true, message: 'abbr is required', trigger: 'blue'}],
          type: [{required: true, message: 'type is required', trigger: 'blue'}]
        },
        downloadLoading: false
      }
    },
    created() {
      this.getSystem()
      this.getModule()
    },
    update(v) {
      console.log(v)
    },
    methods: {
      getModule() {
        fetchModule().then(response => {
          this.modules = response.contents
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      getSystem() {
        this.listLoading = true
        const data = {
          ...this.listQuery,
          page: this.listQuery.page > 0 ? this.listQuery.page - 1 : 0
        }
        fetchSystem(data).then(response => {
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
        this.getSystem()
      },
      sortChange(data) {
        const {prop, order} = data
        if (prop === 'id') {
          this.sortByID(order)
        }
      },
      sortByID(order) {
        if (order === 'ascending') {
          this.listQuery.sort = '+id'
        } else {
          this.listQuery.sort = '-id'
        }
        this.handleFilter()
      },
      resetTemp() {
        this.temp = {
          name: undefined,
          abbr: undefined,
          description: undefined,
          id: undefined,
          type: undefined
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
            createSystem(this.temp).then(() => {
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
            updateSystem(this.temp).then(() => {
              this.dialogFormVisible = false
              this.$notify({
                title: 'Success',
                message: 'Update Successfully',
                type: 'success',
                duration: 2000
              })
              this.getSystem()
            })
          }
        })
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
      handleFetchPv(pv) {
        fetchPv(pv).then(response => {
          this.pvData = response.data.pvData
          this.dialogPvVisible = true
        })
      },
      handleClearFilter() {
        this.listQuery = {
          ...this.listQuery,
          name: ''
        }
      },
      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
          const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
          const data = this.formatJson(filterVal)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'table-list'
          })
          this.downloadLoading = false
        })
      },
      formatJson(filterVal) {
        return this.list.map(v => filterVal.map(j => {
          if (j === 'timestamp') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        }))
      },
      formatType(typeValue) {
        const list = this.sys_type_options.filter(e => e.value === typeValue)
        return list.length === 1 ? list[0].label : ''
      },
      getSortClass: function (key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      },
      handleSelectionChange(val) {
        console.log(typeof val)
        if (typeof val === 'Array') {
          console.log(val[0].id)
          this.selectedSystem = [val]
        } else {
          this.selectedSystem = []
        }
      },
      getTemplateRow(index, row) {
        this.selectedRow = row
        fetchSystem({id: row.id}).then(response => {
          if (response.contents && response.contents.length === 1) {
            let modules = response.contents[0].modules.map(entity=>entity.id)
            this.$refs.module.setCheckedKeys(modules)
          }
        })
      },
      handleModuleSave() {
        if (this.selectedRow) {
          this.selectedRow.modules = this.$refs.module.getCheckedNodes().filter(o => !o.children).map(entity => entity.id)

          updateSystem(this.selectedRow).then(() => {
            this.$notify({
              title: 'Success',
              message: 'update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      },
    }
  }
</script>
