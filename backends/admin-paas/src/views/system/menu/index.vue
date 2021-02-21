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
      <!--<el-button v-waves :loading="downloadLoading"  class="filter-item" type="success" size="mini" icon="el-icon-download"  @click="handleDownload">
        导出
      </el-button>-->
    </div>
    <el-table
      :data="list"
      style="width: 100%"
      row-key="id"
      border
      lazy
      :load="load"
      :tree-props="{children: 'children', hasChildren: 'subCount'}"
    >
      <el-table-column label="菜单名" prop="title" align="center">
        <template slot-scope="{row}">
          <span>{{ row.title }}</span>
        </template>
      </el-table-column>
      <!--
      <el-table-column label="图标" align="center">
        <template slot-scope="{row}">
          <div v-html="test"></div>

        </template>
      </el-table-column>-->
      <el-table-column label="菜单类型" width="" align="center">
        <template slot-scope="{row}">
          <span>{{ getMenuType(row.type) }}</span>
        </template>
      </el-table-column>

      <!-- <el-select v-model="value" placeholder="请选择">
           <el-option
             v-for="item in options"
             :key="item.value"
             :label="item.label"
             :value="item.value">
           </el-option>
         </el-select>-->
      <el-table-column label="组件" width="" align="center">
        <template slot-scope="{row}">
          <span>{{ row.component }}</span>
        </template>
      </el-table-column>
      <el-table-column label="权限标识" width="" align="center">
        <template slot-scope="{row}">
          <span>{{ row.permission }}</span>
        </template>
      </el-table-column>
      <el-table-column label="说明" width="" align="center">
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
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
      @pagination="getMenu"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        :load="load"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
        row-key="id"
        :tree-props="{children: 'children'}">
        <el-form-item label="菜单名" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="temp.type" @change="handleMenuTypeChange">
            <el-radio-button label="0">目录</el-radio-button>
            <el-radio-button label="1">菜单</el-radio-button>
            <el-radio-button label="2">链接</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="组件" prop="component">
          <el-input v-model="temp.component" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="temp.permission" />
        </el-form-item>
        <el-form-item label="Icon" prop="icon">
          <el-input v-model="temp.icon"/>
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
  import {fetchPv, updateArticle} from '@/api/article'
  import {fetchRoles} from '@/api/role'
  import {build, child, createMenu, fetchMenus, fetchRoleMenus} from '@/api/menu'
  import waves from '@/directive/waves' // waves directive
  import {parseTime} from '@/utils'
  import Pagination from '@/components/Pagination' // secondary package based on el-pagination

  export default {
    name: 'ComplexTable',
    components: {Pagination},
    directives: {waves},
    data() {
      return {
        test: '<svg-icon icon-class="404" />',

        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          blurry: undefined,
          isRoot: true
        },
        roleListQuery: {
          blurry: undefined,
          page: 1,
          limit: 1024
        },
        importanceOptions: [1, 2, 3],
        sortOptions: [{label: 'ID Ascending', key: '+id'}, {label: 'ID Descending', key: '-id'}],
        statusOptions: ['published', 'draft', 'deleted'],
        showReviewer: false,
        temp: {
          title: undefined,
          type: undefined,
          component: undefined,
          permission: ''

        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '修改',
          create: '新建'
        },
        dialogPvVisible: false,
        pvData: [],
        rules: {
          username: [{required: true, message: 'type is required', trigger: 'blue'}]
        },
        downloadLoading: false,
        roles: [],
        role: '',
        queryType: 0,
        menuRoot: []
      }
    },
    created() {
      this.getMenu()
    },
    methods: {
      getMenu() {
        this.listLoading = true
        const data = {
          ...this.listQuery,
          page: this.listQuery.page > 0 ? this.listQuery.page - 1 : 0
        }
        build(data).then(response => {
          this.list = response
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      getRoles() {
        this.listLoading = true
        const data = {
          ...this.roleListQuery,
          page: this.roleListQuery.page > 0 ? this.roleListQuery.page - 1 : 0
        }
        fetchRoles(data).then(response => {
          console.log('fetchRoles>>>>>>>>>', fetchRoles)
          this.roles = response.contents
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getMenu()
      },
      handleModifyStatus(row, status) {
        this.$message({
          message: '操作Success',
          type: 'success'
        })
        row.status = status
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
          username: undefined,
          nickname: undefined,
          description: undefined
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
            createMenu(this.temp).then(() => {
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
            const tempData = Object.assign({}, this.temp)
            tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
            updateArticle(tempData).then(() => {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: 'Success',
                message: 'Update Successfully',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },
      load(tree, treeNode, resolve) {
        child({pid: tree.id}).then(response => {
          console.log('xxxxxxxxxxxxxxxxxxxx', response[0])
          resolve(response)

        })
      },
      handleDelete(row, index) {
        console.log(row)

        // deleteUser([row.id]).then(() => {
        //   this.$notify({
        //     title: 'Success',
        //     message: 'Delete Successfully',
        //     type: 'success',
        //     duration: 2000
        //   })
        //   this.list.splice(index, 1)
        // })
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
      handleRoleChange: function (roleid) {
        this.queryType = 1
        this.listLoading = true
        const data = {
          limit: this.listQuery.limit,
          page: 0,
          roleId: roleid
        }
        fetchRoleMenus(data).then(response => {
          this.list = response.contents
          this.total = response.total
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      getSortClass: function (key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      },
      handleMenuTypeChange: function (type) {
        if (type === 0) {
          fetchMenus({type: type}).then(response => {
            response.contents.forEach(menu => {
              this.menuRoot.push((menu.id, menu.title))
            })
          })
        } else {
          this.menuRoot = []
        }
      },
      getMenuType: function (type) {
        switch (type) {
          case 0:
            return '目录'
          case 1:
            return '菜单'
          case 2:
            return '按钮'
          default:
            return ''
        }
      }
    }
  }
</script>
