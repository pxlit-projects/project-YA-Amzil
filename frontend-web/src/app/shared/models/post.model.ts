export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  category: string;
  status: 'draft' | 'published';
  createdDate: Date;
}
