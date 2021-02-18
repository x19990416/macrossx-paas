module.exports = file => {
 console.log('xxxxxx',file)
 return require('@/views/' + file + '.vue').default   
}
