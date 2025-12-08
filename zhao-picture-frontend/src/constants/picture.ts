export const PIC_AUDIT_STATUS_ENUM = {
  REVIEWING: 0,
  REVIEW_PASS: 1,
  REVIEW_FAIL: 2,
}


export const PIC_AUDIT_STATUS_MAP={
  0:'待审核',
  1:'审核通过',
  2:'审核拒绝',
}


export const PIC_AUDIT_STATUS_OPTIONS = Object.keys(PIC_AUDIT_STATUS_MAP).map(key =>{
  return{
    label: PIC_AUDIT_STATUS_MAP[key],
    value: key,
  }
})


//最终 PIC_REVIEW_STATUS_OPTIONS 的值是：
// [
//   { label: '待审核', value: '0' },
//   { label: '通过', value: '1' },
//   { label: '拒绝', value: '2' }
// ]
