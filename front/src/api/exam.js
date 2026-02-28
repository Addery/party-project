// 模拟考试API接口

export const getExams = async () => {
  // 模拟API请求延迟
  await new Promise(resolve => setTimeout(resolve, 500));
  return [];
};

export const createExam = async (examData) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { id: `exam_${Date.now()}`, ...examData };
};

export const updateExam = async (id, examData) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { id, ...examData };
};

export const deleteExam = async (id) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return true;
};

export const publishExam = async (id) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return true;
};

export const getExamParticipants = async (examId) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return [];
};

export const getExamQuestions = async (examId) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return [];
};

export const exportExams = async () => {
  await new Promise(resolve => setTimeout(resolve, 1000));
  return { success: true, message: '导出成功' };
};
