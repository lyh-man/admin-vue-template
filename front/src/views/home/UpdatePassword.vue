<template>
    <el-dialog title="修改密码" :visible.sync="visible" :append-to-body="true">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="100px">
            <el-form-item label="账号">
                <span>{{ userName }}</span>
            </el-form-item>
            <el-form-item label="原密码" prop="password">
                <el-input type="password" v-model="dataForm.password" placeholder="原密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input type="password" v-model="dataForm.newPassword" placeholder="新密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
                <el-input type="password" v-model="dataForm.confirmPassword" placeholder="确认密码"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        data() {
            return {
                userName: 'admin',
                visible: false,
                dataForm: {
                    password: '',
                    newPassword: '',
                    confirmPassword: ''
                },
                dataRule: {
                    password: [{
                        required: true,
                        message: '原密码不能为空',
                        trigger: 'blur'
                    }],
                    newPassword: [{
                        required: true,
                        message: '新密码不能为空',
                        trigger: 'blur'
                    }],
                    confirmPassword: [{
                        required: true,
                        message: '确认密码不能为空',
                        trigger: 'blur'
                    }]
                }
            }
        },
        methods: {
            // 初始化
            init() {
                this.visible = true
                // 初始化清空表单内容
                this.$nextTick(() => {
                    this.$refs['dataForm'].resetFields()
                })
            },
            // 表单提交，回到登录界面
            dataFormSubmit() {
                // TODO: 表达提交逻辑待完成
                alert("表达提交逻辑未完成")
                this.visible = false;
                this.$nextTick(() => {
                    this.$router.push({
                        name: "Login"
                    })
                })
            }
        }
    }
</script>