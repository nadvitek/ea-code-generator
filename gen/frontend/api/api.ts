export * from './books.service';
import { BooksService } from './books.service';
export * from './libraries.service';
import { LibrariesService } from './libraries.service';
export * from './users.service';
import { UsersService } from './users.service';
export const APIS = [BooksService, LibrariesService, UsersService];
