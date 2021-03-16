<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.blurry"
        placeholder="产品名"
        size="small"
        style="width: 200px;"
        class="filter-item filter-time-margin"
      />
      <el-button

        class="filter-item"
        type="success"
        size="small"
        icon="el-icon-search"
        style="margin-left: 10px;"

      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="warning"
        size="small"
        icon="el-icon-refresh"
      >
        重置
      </el-button>
    </div>
    <div class="filter-container">
      <el-button class="filter-item" type="primary" size="small" icon="el-icon-plus">
        新增
      </el-button>
    </div>
    <div class="filter-container">
      <el-table :data="products" v-loading="listLoading" row-key="id">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="配料表">
                <span>
                  <a v-for="target in props.row.plb.split('、')" :href="getIngredientUrl(target)" >
                    {{target}}
                  </a>
                </span>

              </el-form-item>
            </el-form>
          </template>

        </el-table-column>

        <el-table-column label='品名' prop="name" align="center" />
        <el-table-column label='品牌' prop="brand" align="center"/>
        <el-table-column label='备案号' prop="approval" align="center"/>
        <el-table-column label='状态' prop="status" align="center"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width"/>
      </el-table>
      <Pagination
        v-show="total>0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="fetch"
      />
    </div>
  </div>
</template>
<script>
  import {fetchProduct} from '@/api/nf'
  import Pagination from '@/components/Pagination' // secondary package based on el-pagination

  export default {
    components: {Pagination},
    data() {
      return {
        listLoading: false,
        products: [],
        total:0,
        listQuery: {
          page: 1,
          size: 10,
          blurry: undefined,
          brand: undefined,
          status: undefined
        },
      }
    },
    created() {
      this.fetch()
    },
    methods: {
      fetch() {
        this.listLoading = true
        const data = {
          ...this.listQuery,
          page: this.listQuery.page > 0 ? this.listQuery.page - 1 : 0
        }
        fetchProduct(data).then(res => {
          this.products = res.contents;
          this.listLoading = false
          this.total = res.total
        })
      },
      getIngredientUrl(target) {
        return "http://www.baidu.com?target="+target
      }
    }
  }
</script>
