export class Comment {
  id?: number;
  postId: number;
  author: string;
  content: string;
  createAt: Date;
  updateAt: Date;

  constructor(
    postId: number,
    author: string,
    content: string,
    createAt: string,
    updateAt: string
  ) {
    this.postId = postId;
    this.author = author;
    this.content = content;
    this.createAt = new Date(createAt);
    this.updateAt = new Date(updateAt);
  }
}
